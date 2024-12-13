package org.sahabat.pulau.spesification;

import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
public class CustomSpecification<T> implements Specification<T> {
    private final SearchCriteria criteria;
    private final SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");
    private final DateTimeFormatter formatLocalDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final DateTimeFormatter formatLocalDateTime = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public CustomSpecification(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder builder) {

        switch (criteria.getOperation()) {
            case LIKE: {
                if (criteria.getKey().contains("/")) {
                    String[] variables = criteria.getKey().split("/");
                    List<Predicate> listOfPredicate = new ArrayList<>();

                    for (int i = 0; i < criteria.getKey().split("/").length; i++) {
                        Predicate predicate = builder.like(builder.lower(getField(root,variables[i]).as(String.class)),
                                "%" + criteria.getValue().toString().toLowerCase() + "%");
                        listOfPredicate.add(predicate);
                    }
                    return builder.or(listOfPredicate.toArray(new Predicate[listOfPredicate.size()]));
                } else {
                    return builder.like(
                            builder.lower(
                                    getField(root,criteria.getKey()).as(String.class)),
                            "%" + criteria.getValue().toString().toLowerCase() + "%");

                }
            }
            case EQUAL: {
                if (criteria.getKey().contains("/")) {
                    String[] variables = criteria.getKey().split("/");
                    List<Predicate> listOfPredicate = new ArrayList<>();

                    for (int i = 0; i < criteria.getKey().split("/").length; i++) {
                        Predicate predicate = builder.equal(getField(root,variables[i]), criteria.getValue());
                        listOfPredicate.add(predicate);
                    }
                    return builder.or(listOfPredicate.toArray(new Predicate[listOfPredicate.size()]));
                } else {
                    return builder.equal(getField(root,criteria.getKey()), criteria.getValue());
                }
            }
            case INT_GREATER_THAN: {
                return builder.greaterThanOrEqualTo(getField(root,criteria.getKey()).as(Integer.class), (Integer) criteria.getValue());
            }
            case INT_LESS_THAN: {
                return builder.lessThanOrEqualTo(getField(root, criteria.getKey()).as(Integer.class), (Integer) criteria.getValue());
            }
            case DATE_GREATER_THAN: {
                return builder.greaterThan(getField(root, criteria.getKey()).as(Date.class), (Date) criteria.getValue());
            }
            case DATE_LESS_THAN: {
                return builder.lessThan(getField(root, criteria.getKey()).as(Date.class), (Date) criteria.getValue());
            }
            case DATE_GREATER_THAN_EQUAL: {
                return builder.greaterThanOrEqualTo(getField(root, criteria.getKey()).as(Date.class), (Date) criteria.getValue());
            }
            case DATE_LESS_THAN_EQUAL: {
                return builder.lessThanOrEqualTo(getField(root, criteria.getKey()).as(Date.class), (Date) criteria.getValue());
            }
            case LOCAL_DATE_GREATER_THAN: {
                return builder.greaterThan(getField(root, criteria.getKey()).as(LocalDate.class), (LocalDate) criteria.getValue());
            }
            case LOCAL_DATE_LESS_THAN: {
                return builder.lessThan(getField(root, criteria.getKey()).as(LocalDate.class), (LocalDate) criteria.getValue());
            }
            case LOCAL_DATE_REVERSE_BETWEEN: {
                String[] fields = criteria.getKey().split("-");
                try {
                    log.info("criteria value = {}",criteria.getValue());
                    LocalDate date = (LocalDate) criteria.getValue();
                    String start = fields[0];
                    String end = fields[1];

                    return builder.between(builder.literal(date), root.get(start), root.get(end));
                } catch (Exception e) {
                    log.error("ERROR PARSE SPECIFICATION BETWEEN DATE",e);
                }
                break;            }
            case EQUAL_OR_NULL: {
                Predicate p1 = builder.equal(getField(root, criteria.getKey()), criteria.getValue());
                Predicate p2 = builder.isNull(getField(root, criteria.getKey()));
                return builder.or(p1, p2);
            }
            case IS_NULL: {
                return builder.isNull(getField(root,criteria.getKey()));
            }
            case NOT_NULL: {
                return builder.isNotNull(getField(root,criteria.getKey()));
            }
            case NOT_EQUAL: {
                return builder.notEqual(getField(root,criteria.getKey()), criteria.getValue());
            }
            case BETWEEN: {
                String[] dateStr = criteria.getValue().toString().split(" - ");
                try {

                    Date end = formatDate.parse(dateStr[1]);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(end);
                    cal.set(Calendar.HOUR_OF_DAY, 23);
                    cal.set(Calendar.MINUTE, 59);
                    cal.set(Calendar.SECOND, 59);
                    end = cal.getTime();
                    log.debug("SEARCH " + criteria.getKey() + " BETWEEN DATE " + formatDate.parse(dateStr[0]) + " and " + end);

                    return builder.between(getField(root,criteria.getKey()).as(Date.class), formatDate.parse(dateStr[0]), end);

                }catch (Exception e){
                    log.error("ERROR PARSE SPECIFICATION BETWEEN DATE",e);
                }
                break;
            }
            case LOCAL_DATE_BETWEEN: {
                String[] dateStr = criteria.getValue().toString().split(" - ");
                try {
                    LocalDate dateStart = LocalDate.parse(dateStr[0],formatLocalDate);
                    LocalDate dateEnd = LocalDate.parse(dateStr[1],formatLocalDate);

                    return builder.between(getField(root,criteria.getKey()).as(LocalDate.class), dateStart, dateEnd);
                } catch (Exception e) {
                    log.error("ERROR PARSE SPECIFICATION BETWEEN DATE",e);
                }
                break;
            }
            case LOCAL_DATE_TIME_BETWEEN: {
                String[] dateStr = criteria.getValue().toString().split(" - ");
                Boolean onlyDate = criteria.getValue().toString().length() == 23;
                try{
                    LocalDateTime dateStart = LocalDateTime.parse(dateStr[0] + (onlyDate ? " 00:00:00" : ""), formatLocalDateTime);
                    LocalDateTime dateEnd = LocalDateTime.parse(dateStr[1] + (onlyDate ? " 23:59:59" : ""), formatLocalDateTime);

                    return builder.between(getField(root, criteria.getKey()).as(LocalDateTime.class), dateStart, dateEnd);
                } catch (Exception e) {
                    log.error("ERROR PARSE SPECIFICATION BETWEEN DATE",e);
                }
                break;
            }
            case IN: {
                return getField(root,criteria.getKey()).in((List<String>) criteria.getValue());
            }
            case NOT_IN: {
                return getField(root,criteria.getKey()).in((List<Object>) criteria.getValue()).not();
            }
            case MONTH: {
                return builder.equal(builder.function("MONTH", Integer.class, getField(root,criteria.getKey())), criteria.getValue());
            }
            case YEAR: {
                return builder.equal(builder.function("YEAR", Integer.class, getField(root,criteria.getKey())), criteria.getValue());
            }
            case EQUAL_DATE: {
                try {
                    Date date = formatDate.parse(criteria.getValue().toString());
                    return builder.equal(getField(root,criteria.getKey()), date);
                } catch (ParseException e) {
                    log.error("ERROR PARSE SPECIFICATION EQUAL DATE",e);
                }
            }
            case DAY_MONTH_YEAR: {
                int day = Integer.parseInt(criteria.getValue().toString().split("/")[0]);
                int month = Integer.parseInt(criteria.getValue().toString().split("/")[1]);
                int year = Integer.parseInt(criteria.getValue().toString().split("/")[2]);

                Predicate eDate = builder.equal(builder.function("DAY", Integer.class, getField(root,criteria.getKey())), day);
                Predicate eMonth = builder.equal(builder.function("MONTH", Integer.class, getField(root,criteria.getKey())), month);
                Predicate eYear = builder.equal(builder.function("YEAR", Integer.class, getField(root,criteria.getKey())), year);
                return builder.and(eDate, eMonth, eYear);
            }
            case MONTH_YEAR: {
                //query based month of the date
                int month = Integer.parseInt(criteria.getValue().toString().split("-")[0]);
                int year = Integer.parseInt(criteria.getValue().toString().split("-")[1]);

                Predicate eMonth = builder.equal(builder.function("MONTH", Integer.class, getField(root,criteria.getKey())), month);
                Predicate eYear = builder.equal(builder.function("YEAR", Integer.class, getField(root,criteria.getKey())), year);
                return builder.and(eMonth, eYear);
            }
            case QUARTERLY_YEAR: {
                // assuming value = 1-2023
                String quarter = criteria.getValue().toString().split("-")[0];
                String yearStr = criteria.getValue().toString().split("-")[1];

                int startMonth = Integer.parseInt(quarter) * 3 - 2;
                int endMonth = Integer.parseInt(quarter) * 3;
                int year = Integer.parseInt(yearStr);

                LocalDateTime start = YearMonth.of(year,startMonth).atDay(1).atStartOfDay();
                LocalDateTime end = YearMonth.of(year,endMonth).atEndOfMonth().atTime(23,59,59);

                return builder.between(getField(root,criteria.getKey()).as(LocalDateTime.class), start, end);
            }
            case LAST_30_DAYS: {
                Date start = new Date();
                Calendar cal = Calendar.getInstance();
                cal.setTime(start);
                cal.set(Calendar.HOUR_OF_DAY, 23);
                cal.set(Calendar.MINUTE, 59);
                cal.set(Calendar.SECOND, 59);
                Date endDate = cal.getTime();

                cal.add(Calendar.DATE,-30);

                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                Date startDate = cal.getTime();

                log.debug("SEARCH " + criteria.getKey() + " BETWEEN DATE " + formatDate.format(startDate) + " and " + formatDate.format(endDate));

                return builder.between(getField(root,criteria.getKey()).as(Date.class), startDate, endDate);
            }
            case LAST_6_MONTH: {
                Date date = new Date();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.MONTH,-5);

                String sDate = formatDate.format(date);
                int month = Integer.valueOf(sDate.split("/")[1]);
                int year = Integer.valueOf(sDate.split("/")[2]);

//                int endMonth = calendar.

                Predicate eMonth = builder.between(builder.function("MONTH", Integer.class, getField(root,criteria.getKey())), calendar.get(Calendar.MONTH),month);
                Predicate eYear = builder.equal(builder.function("YEAR", Integer.class, getField(root,criteria.getKey())), year);
                return builder.and(eMonth, eYear);
            }
            case DATE_DAY_MONTH_YEAR: {
                String sDate = formatDate.format(criteria.getValue());

                int day = Integer.valueOf(sDate.split("/")[0]);
                int month = Integer.valueOf(sDate.split("/")[1]);
                int year = Integer.valueOf(sDate.split("/")[2]);

                Predicate eDate = builder.equal(builder.function("DAY", Integer.class, getField(root,criteria.getKey())), day);
                Predicate eMonth = builder.equal(builder.function("MONTH", Integer.class, getField(root,criteria.getKey())), month);
                Predicate eYear = builder.equal(builder.function("YEAR", Integer.class, getField(root,criteria.getKey())), year);
                return builder.and(eDate, eMonth, eYear);
            }
            default: {
                log.info("handling for this operation [" + criteria.getOperation() + "] is not found!");
            }
        }
        return null;
    }

    private Path<?> getField(Root<T> root, String key) {

        Path<?> field = root.get(key.split("-")[0]);

        if (key.contains("-")) {
            for (int i = 1; i < key.split("-").length; i++) {
                field = field.get(key.split("-")[i]);
            }
        }

        return field;
    }

    private Join<?, ?> getJoinField(Root<T> root, String key) {

        Join<?, ?> field = root.join(key.split("-")[0]);

        if (key.contains("-")) {
            for (int i = 1; i < key.split("-").length; i++) {
                field = field.join(key.split("-")[i]);
            }
        }

        return field;
    }

    private Join<?, ?> getLeftJoinField(Root<T> root, String key) {

        Join<?, ?> field = root.join(key.split("-")[0], JoinType.LEFT);

        if (key.contains("-")) {
            for (int i = 1; i < key.split("-").length; i++) {
                field = field.join(key.split("-")[i]);
            }
        }

        return field;
    }

}
