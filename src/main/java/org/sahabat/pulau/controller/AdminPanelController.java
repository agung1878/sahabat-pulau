package org.sahabat.pulau.controller;


import org.sahabat.pulau.dao.DonationDao;
import org.sahabat.pulau.dto.DonationSearchParameter;
import org.sahabat.pulau.entity.Donation;
import org.sahabat.pulau.services.DonationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller @RequestMapping("/admin_panel")
public class AdminPanelController {

    @Autowired private DonationService service;

    @GetMapping("/dashboard")
    public String dashboard() {
        return "admin_panel/dashboard";
    }

    @GetMapping("/donation")
    public String donation(DonationSearchParameter params, ModelMap modelMap,
                           @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<Donation> result = service.getPage(params, pageable);


        modelMap.addAttribute("datas", result);
        modelMap.addAttribute("params", params);


        return "admin_panel/donation";
    }

    @GetMapping("donatur")
    public String donatur() {
        return "admin_panel/donatur";
    }

}
