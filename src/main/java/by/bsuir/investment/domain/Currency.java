package by.bsuir.investment.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/*
create table currencies(
	code char(3) primary key
);
 */

@Data
@Entity
@Table(name = "currencies")
public class Currency {
    @Id
    @Column(name = "code", length = 3)
    private String code;

    @Column(name = "exchange_rate")
    private Float exchangeRate;
}
