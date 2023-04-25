package by.bsuir.investment.domain;

import lombok.Data;

import javax.persistence.*;

/*
create table investment_projects(
	code varchar(32) primary key,
	object varchar(256) not null,
	implementation_period int not null,
	investments float not null,
    currency_code char(3) not null references currencies(code) on delete cascade on update cascade,
    measurement_unit varchar(16) not null
);
 */

@Data
@Entity
@Table(name = "investment_projects")
public class InvestmentProject {
    @Id
    @Column(name = "code", nullable = false, length = 32)
    private String code;

    @Column(name = "object", nullable = false, length = 256)
    private String object;

    @Column(name = "implementation_period", nullable = false)
    private Integer implementationPeriod;

    @Column(name = "investments", nullable = false)
    private Float investments;

    @ManyToOne(fetch = FetchType.EAGER)
    private Currency currency;
}
