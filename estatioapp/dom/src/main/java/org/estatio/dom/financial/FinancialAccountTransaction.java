package org.estatio.dom.financial;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.DatastoreIdentity;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Index;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Queries;
import javax.jdo.annotations.Query;
import javax.jdo.annotations.Version;
import javax.jdo.annotations.VersionStrategy;

import org.joda.time.LocalDate;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.annotation.Where;

import org.isisaddons.module.security.dom.tenancy.ApplicationTenancy;

import org.estatio.app.security.EstatioRole;
import org.estatio.dom.EstatioDomainObject;
import org.estatio.dom.JdoColumnLength;
import org.estatio.dom.JdoColumnScale;
import org.estatio.dom.apptenancy.WithApplicationTenancyCountry;
import org.estatio.dom.utils.TitleBuilder;

import lombok.Getter;
import lombok.Setter;

@PersistenceCapable(identityType = IdentityType.DATASTORE)
@DatastoreIdentity(strategy = IdGeneratorStrategy.IDENTITY, column = "id")
@Version(strategy = VersionStrategy.VERSION_NUMBER, column = "version")
@Queries({
        @Query(
                name = "findByFinancialAccount",
                language = "JDOQL",
                value = "SELECT FROM org.estatio.dom.financial.FinancialAccountTransaction "
                        + "WHERE financialAccount == :financialAccount"),
        @Query(
                name = "findByFinancialAccountAndTransactionDate",
                language = "JDOQL",
                value = "SELECT FROM org.estatio.dom.financial.FinancialAccountTransaction "
                        + "WHERE financialAccount == :financialAccount && "
                        + "transactionDate == :transactionDate"),
        @Query(
                name = "findByFinancialAccountAndTransactionDateAndSequence",
                language = "JDOQL",
                value = "SELECT FROM org.estatio.dom.financial.FinancialAccountTransaction "
                        + "WHERE financialAccount == :financialAccount && "
                        + "transactionDate == :transactionDate && "
                        + "sequence == :sequence")
})
@Index(
        name = "FinancialAccountTransaction_financialAccount_transactionDate_IDX",
        members = { "financialAccount", "transactionDate" })
public class FinancialAccountTransaction
        extends EstatioDomainObject<FinancialAccountTransaction>
        implements WithApplicationTenancyCountry {

    public FinancialAccountTransaction() {
        super("financialAccount,transactionDate,description,amount");
    }

    public String title() {
        return TitleBuilder.start()
                .withParent(getFinancialAccount())
                .withName(getAmount().toString())
                .withName(getTransactionDate().toString())
                .toString();
    }

    // //////////////////////////////////////

    @PropertyLayout(
            named = "Application Level",
            describedAs = "Determines those users for whom this object is available to view and/or modify."
    )
    public ApplicationTenancy getApplicationTenancy() {
        return getFinancialAccount().getApplicationTenancy();
    }

    // //////////////////////////////////////

    @Column(name = "financialAccountId", allowsNull = "false")
    @MemberOrder(sequence = "1")
    @Getter @Setter
    FinancialAccount financialAccount;

    // //////////////////////////////////////

    @Column(allowsNull = "false")
    @MemberOrder(sequence = "2")
    @Getter @Setter
    LocalDate transactionDate;

    // //////////////////////////////////////

    @Column(allowsNull = "false")
    @Property(hidden = Where.EVERYWHERE)
    @Getter @Setter
    private BigInteger sequence;

    // //////////////////////////////////////


    @Column(allowsNull = "true", length = JdoColumnLength.DESCRIPTION)
    @MemberOrder(sequence = "4")
    @Property(hidden = Where.ALL_TABLES)
    @Getter @Setter
    String description;

    // //////////////////////////////////////

    @Column(allowsNull = "false", scale = JdoColumnScale.MONEY)
    @MemberOrder(sequence = "5")
    @Getter @Setter
    BigDecimal amount;

    @Action(semantics = SemanticsOf.IDEMPOTENT)
    public FinancialAccountTransaction changeTransactionDetails(
            final BigDecimal amount,
            final LocalDate transactionDate,
            @Parameter(optionality = Optionality.OPTIONAL)
            final String description
            ){
        setAmount(amount);
        setDescription(description);
        setTransactionDate(transactionDate);
        return this;
    }

    public boolean hideChangeTransactionDetails(
            final BigDecimal amount,
            final LocalDate transactionDate,
            final String description){
        return !EstatioRole.ADMINISTRATOR.isApplicableFor(getUser());
    }

    public BigDecimal default0ChangeTransactionDetails(){
        return getAmount().setScale(JdoColumnScale.MONEY);
    }

    public LocalDate default1ChangeTransactionDetails(){
        return getTransactionDate();
    }

    public String default2ChangeTransactionDetails(){
        return getDescription();
    }


}
