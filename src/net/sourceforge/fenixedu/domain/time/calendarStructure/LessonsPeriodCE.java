package net.sourceforge.fenixedu.domain.time.calendarStructure;

import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.joda.time.DateTime;

public class LessonsPeriodCE extends LessonsPeriodCE_Base {

    public LessonsPeriodCE(AcademicCalendarEntry academicCalendarEntry, MultiLanguageString title, 	    
	    MultiLanguageString description, DateTime begin, DateTime end, AcademicCalendarRootEntry rootEntry) {

	super();
	super.initEntry(academicCalendarEntry, title, description, begin, end, rootEntry);
    }
    
    private LessonsPeriodCE(AcademicCalendarEntry parentEntry, MultiLanguageString title, 	    
	    MultiLanguageString description, DateTime begin, DateTime end, AcademicCalendarEntry templateEntry) {

	super();
	super.initEntry(parentEntry, title, description, begin, end, parentEntry.getRootEntry());
	setTemplateEntry(templateEntry);
    }

    private LessonsPeriodCE(AcademicCalendarEntry entry, LessonsPeriodCE lessonsPeriodCE) {
	super();
	super.initVirtualOrRedefinedEntry(entry, lessonsPeriodCE);
    }

    @Override
    public boolean isLessonsPerid() {
	return true;
    }  

    @Override
    protected boolean isParentEntryInvalid(AcademicCalendarEntry parentEntry) {
	return !parentEntry.isAcademicSemester() && !parentEntry.isAcademicTrimester();
    }

    @Override
    protected boolean exceededNumberOfChildEntries(AcademicCalendarEntry childEntry) {
	return false;
    }

    @Override
    protected boolean areIntersectionsPossible() {	
	return false;
    }

    @Override
    protected boolean areOutOfBoundsPossible() {	
	return false;
    }

    @Override
    protected AcademicCalendarEntry makeAnEntryCopyInDifferentCalendar(AcademicCalendarEntry parentEntry, boolean virtual) {
	if(virtual) {
	    return new LessonsPeriodCE(parentEntry, this);
	} else {
	    return new LessonsPeriodCE(parentEntry, getTitle(), getDescription(), getBegin(), getEnd(), this);	    
	}
    }
}
