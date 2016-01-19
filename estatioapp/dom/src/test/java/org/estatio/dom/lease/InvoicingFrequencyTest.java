package org.estatio.dom.lease;

import org.apache.isis.core.unittestsupport.jmocking.JUnitRuleMockery2;
import org.estatio.dom.invoice.InvoicingInterval;
import org.estatio.dom.valuetypes.LocalDateInterval;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.joda.time.LocalDate;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class InvoicingFrequencyTest {

    @Test
    public void testIntervalContaining() {
        testRange(InvoicingFrequency.MONTHLY_IN_ADVANCE, "2010-01-01", "2010-01-01/2010-02-01");
        testRange(InvoicingFrequency.QUARTERLY_IN_ADVANCE, "2010-01-01", "2010-01-01/2010-04-01");
        testRange(InvoicingFrequency.QUARTERLY_IN_ADVANCE_PLUS1M, "2009-11-01", "2009-11-01/2010-02-01");
        testRange(InvoicingFrequency.QUARTERLY_IN_ADVANCE_PLUS1M, "2010-02-01", "2010-02-01/2010-05-01");
        testRange(InvoicingFrequency.QUARTERLY_IN_ADVANCE_PLUS1M, "2010-05-01", "2010-05-01/2010-08-01");
        testRange(InvoicingFrequency.QUARTERLY_IN_ADVANCE_PLUS1M, "2010-08-01", "2010-08-01/2010-11-01");
        testRange(InvoicingFrequency.QUARTERLY_IN_ADVANCE_PLUS2M, "2009-12-01", "2009-12-01/2010-03-01");
        testRange(InvoicingFrequency.QUARTERLY_IN_ADVANCE_PLUS2M, "2010-03-01", "2010-03-01/2010-06-01");
        testRange(InvoicingFrequency.QUARTERLY_IN_ADVANCE_PLUS2M, "2010-06-01", "2010-06-01/2010-09-01");
        testRange(InvoicingFrequency.QUARTERLY_IN_ADVANCE_PLUS2M, "2010-09-01", "2010-09-01/2010-12-01");
        testRange(InvoicingFrequency.MONTHLY_IN_ADVANCE, "2010-01-01", "2010-01-01/2010-02-01");
        testRange(InvoicingFrequency.YEARLY_IN_ARREARS, "2010-01-01", "2010-01-01/2011-01-01");
    }

    @Test
    public void testIntervalsInRange() {
        List<InvoicingInterval> intervalsInRange = InvoicingFrequency.QUARTERLY_IN_ADVANCE.intervalsInRange(new LocalDate(2012, 1, 1), new LocalDate(2014, 4, 1));
        assertThat(intervalsInRange.size(), is(9));
    }

    @Test
    public void testIntervalsInDueDateRange() {
        dueDateRangeTester(InvoicingFrequency.QUARTERLY_IN_ADVANCE, "2012-01-01/2014-04-01", 9);
        dueDateRangeTester(InvoicingFrequency.QUARTERLY_IN_ADVANCE, "2013-12-31/2014-04-01", 1);
        dueDateRangeTester(InvoicingFrequency.QUARTERLY_IN_ADVANCE, "2013-12-30/2013-12-31", 0);
        dueDateRangeTester(InvoicingFrequency.QUARTERLY_IN_ADVANCE, "2013-12-29/2013-12-30", 0);
        dueDateRangeTester(InvoicingFrequency.QUARTERLY_IN_ARREARS, "2013-12-29/2013-12-30", 0);
        dueDateRangeTester(InvoicingFrequency.QUARTERLY_IN_ADVANCE, "2014-01-02/2014-01-03", 0);
        dueDateRangeTester(InvoicingFrequency.QUARTERLY_IN_ARREARS, "2014-01-02/2014-01-03", 0);
        dueDateRangeTester(InvoicingFrequency.QUARTERLY_IN_ARREARS, "2013-12-29/2014-03-31", 1);
        dueDateRangeTester(InvoicingFrequency.QUARTERLY_IN_ARREARS, "2013-12-29/2014-04-01", 2);
        dueDateRangeTester(InvoicingFrequency.QUARTERLY_IN_ADVANCE, "2013-12-29/2014-01-02", 1);
        dueDateRangeTester(InvoicingFrequency.QUARTERLY_IN_ADVANCE, "2014-01-01/2014-01-01", 0);
        dueDateRangeTester(InvoicingFrequency.QUARTERLY_IN_ADVANCE, "2014-01-01/2014-01-02", 1);
        dueDateRangeTester(InvoicingFrequency.QUARTERLY_IN_ADVANCE, "2012-01-01/2014-01-01", 8);
        dueDateRangeTester(InvoicingFrequency.QUARTERLY_IN_ADVANCE_PLUS2M, "2012-01-01/2012-03-01", 0);
        dueDateRangeTester(InvoicingFrequency.QUARTERLY_IN_ADVANCE_PLUS2M, "2012-01-01/2012-03-02", 1);
        dueDateRangeTester(InvoicingFrequency.QUARTERLY_IN_ADVANCE_PLUS2M, "2012-03-01/2012-03-02", 1);
        dueDateRangeTester(InvoicingFrequency.FIXED_IN_ADVANCE, "2012-03-01/2012-03-02", 1);
        dueDateRangeTester(InvoicingFrequency.FIXED_IN_ARREARS, "2012-03-01/2012-03-02", 1);
    }

    @Test
    public void testIntervalsInDueDateRangeWithInterval() {
        dueDateRangeTester(InvoicingFrequency.QUARTERLY_IN_ADVANCE, "2012-01-01/2014-01-01", "2013-01-15/2014-01-16", "2012-01-01/2012-04-01:2012-01-01", 8);
        dueDateRangeTester(InvoicingFrequency.FIXED_IN_ARREARS, "2012-01-01/2014-01-01", "2012-01-01/2014-01-01", "2012-01-01/2014-01-01:2013-12-31", 1);
        dueDateRangeTester(InvoicingFrequency.FIXED_IN_ARREARS, "2012-01-01/2014-04-01", "2015-02-15/2015-06-16", "2015-02-15/2015-06-16:2015-06-15", 1);
        dueDateRangeTester(InvoicingFrequency.FIXED_IN_ARREARS, "2012-01-01/2014-04-01", "2011-01-31/2012-01-30", "2011-01-31/2012-01-30:2012-01-29", 1);
        dueDateRangeTester(InvoicingFrequency.FIXED_IN_ARREARS, "2012-01-01/2014-04-01", "2011-01-31/2011-12-31", "2011-01-31/2011-12-31:2011-12-30", 1);
        dueDateRangeTester(InvoicingFrequency.FIXED_IN_ADVANCE, "2012-01-01/2014-04-01", "2011-01-31/2011-12-31", "2011-01-31/2011-12-31:2011-01-31", 1);
    }


    private void dueDateRangeTester(
            final InvoicingFrequency frequency,
            final String intervalStr,
            final int result) {
        List<InvoicingInterval> intervalsInDueDateRange =
                frequency.intervalsInDueDateRange(
                        LocalDateInterval.parseString(intervalStr),
                        LocalDateInterval.parseString(intervalStr));
        assertThat(intervalsInDueDateRange.size(), is(result));
    }

    private void dueDateRangeTester(
            final InvoicingFrequency frequency,
            final String periodIntervalStr,
            final String sourceIntervalStr,
            final String expectedIntervalStr,
            final int expectedSize) {
        List<InvoicingInterval> intervalsInDueDateRange =
                frequency.intervalsInDueDateRange(
                        LocalDateInterval.parseString(periodIntervalStr),
                        LocalDateInterval.parseString(sourceIntervalStr));
        String result = intervalsInDueDateRange.size() == 0 ? null : intervalsInDueDateRange.get(0).toString();
        assertThat(intervalsInDueDateRange.size(), is(expectedSize));
        assertThat(result, is(expectedIntervalStr));
    }

    private void testRange(
            final InvoicingFrequency frequency,
            final String dateStr,
            final String expectedStr) {
        assertThat(frequency.intervalContaining(new LocalDate(dateStr)).asLocalDateInterval(),
                is(LocalDateInterval.parseString(expectedStr)));
    }


    public static class FixedGivesAlwaysSameInvoicingInterval extends InvoicingFrequencyTest {


        @Rule
        public JUnitRuleMockery2 context = JUnitRuleMockery2.createFor(JUnitRuleMockery2.Mode.INTERFACES_AND_CLASSES);

        @Mock
        LocalDateInterval anyRangeInterval;

        @Mock
        LocalDateInterval anySourceInterval;

        @Test
        public void alwaysTheSameInvoicingInterval() {

            context.checking(new Expectations() {
                {
                    allowing(anySourceInterval).startDate();
                    allowing(anySourceInterval).endDate();
                    allowing(anySourceInterval).endDateExcluding();
                }
            });

            // given
            InvoicingFrequency fixedFrequencyInAdvance = InvoicingFrequency.FIXED_IN_ADVANCE;

            // when
            List<InvoicingInterval> intervalsInDueDateRange = fixedFrequencyInAdvance.intervalsInDueDateRange(
                    anyRangeInterval,
                    anySourceInterval);

            //then
            assertThat(intervalsInDueDateRange.size(), is(1));

            // and

            // given
            InvoicingFrequency fixedFrequencyInArrears = InvoicingFrequency.FIXED_IN_ARREARS;

            // when
            List<InvoicingInterval> intervalsInDueDateRange2 = fixedFrequencyInArrears.intervalsInDueDateRange(
                    anyRangeInterval,
                    anySourceInterval);

            //then
            assertThat(intervalsInDueDateRange2.size(), is(1));

        }

    }

}
