package org.sahabat.pulau.dao;

import org.sahabat.pulau.entity.Donation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DonationDao extends JpaRepository<Donation, String>, JpaSpecificationExecutor<Donation> {
}
