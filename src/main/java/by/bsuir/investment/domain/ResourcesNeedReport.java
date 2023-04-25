package by.bsuir.investment.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/*
create table resources_need_reports(
	id int primary key auto_increment,
	investment_project_code varchar(32) not null references investment_projects(code) on delete cascade on update cascade,
	date_time datetime not null,
    projects_codes varchar(1024) not null,
    own_investment_resources float not null
);
 */

@Data
@NoArgsConstructor
@Entity
@Table(name = "resources_need_reports")
public class ResourcesNeedReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;

    @Column(name = "projects", nullable = false, length = 2048)
    private String projectsJson;

    @Column(name = "own_investment_resources", nullable = false)
    private Float ownInvestmentResources;

    @ManyToOne(fetch = FetchType.EAGER)
    private Currency currency;

    public ResourcesNeedReport(String projectsJson, Float ownInvestmentResources, Currency currency) {
        this.projectsJson = projectsJson;
        this.ownInvestmentResources = ownInvestmentResources;
        this.currency = currency;
        this.dateTime = LocalDateTime.now();
    }
}
