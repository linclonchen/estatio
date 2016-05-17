package org.estatio.dom.lease;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Unique;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.annotation.DomainObject;

import org.isisaddons.module.security.dom.tenancy.ApplicationTenancy;

import org.estatio.dom.EstatioDomainObject;
import org.estatio.dom.apptenancy.WithApplicationTenancyPropertyLocal;

import lombok.Getter;
import lombok.Setter;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(
        strategy = IdGeneratorStrategy.NATIVE,
        column = "id")
@javax.jdo.annotations.Version(
        strategy = VersionStrategy.VERSION_NUMBER,
        column = "version")
@javax.jdo.annotations.Queries({
        @javax.jdo.annotations.Query(
                name = "findByItem",
                language = "JDOQL",
                value = "SELECT "
                        + "FROM org.estatio.dom.lease.LeaseItemSource "
                        + "WHERE item == :item ")})
@Unique(name = "LeaseItemSource_item_sourceItem_UNQ", members = {"item", "sourceItem"})
@DomainObject
public class LeaseItemSource extends EstatioDomainObject<LeaseItemSource> implements WithApplicationTenancyPropertyLocal {

    public LeaseItemSource() {
        super("item, sourceItem");
    }

    public LeaseItemSource(LeaseItem item, LeaseItem sourceItem) {
        super("item, sourceItem");
        this.item = item;
        this.sourceItem = sourceItem;
    }

    @Override public ApplicationTenancy getApplicationTenancy() {
        return item.getApplicationTenancy();
    }

    @Getter @Setter
    @Column(allowsNull = "false")
    private LeaseItem item;

    @Getter @Setter
    @Column(allowsNull = "false")
    private LeaseItem sourceItem;

}