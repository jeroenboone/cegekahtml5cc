package be.cegeka.eventualizr.application;

import static be.cegeka.eventualizr.web.test.infrastructure.CommonAssert.assertMeetingTO;
import static org.fest.assertions.api.Assertions.assertThat;

import java.util.List;

import org.joda.time.LocalDateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import be.cegeka.eventualizr.application.to.MeetingTO;
import be.cegeka.eventualizr.domain.Meeting;
import be.cegeka.eventualizr.domain.MeetingForTests;
import be.cegeka.eventualizr.domain.MeetingRepository;
import be.cegeka.eventualizr.domain.Talk;
import be.cegeka.eventualizr.domain.TalkForTests;
import be.cegeka.eventualizr.web.test.infrastructure.DBSeeder;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:application-context.xml",
		"classpath:datasource-test-context.xml" })
@Transactional
public class MeetingServiceIntegrationTest {

	@Autowired
	private DBSeeder dbSeeder;
	@Autowired
	private MeetingService meetingService;
	@Autowired
	private MeetingRepository meetingRepository;

	private Meeting expectedMeeting1;
	private Meeting expectedMeeting2;
	private Talk talk1;
	private Talk talk2;
	private LocalDateTime start;
	private LocalDateTime end;

	@Before
	public void setUp() throws Exception {
		start = new LocalDateTime(2013,
				1, 30, 16, 0);
		end = new LocalDateTime(2013, 1, 30, 20, 0);
		expectedMeeting1 = MeetingForTests.withDefaults(start, end);
		expectedMeeting2 = MeetingForTests.withDefaults(new LocalDateTime(2013,
				1, 30, 13, 0), new LocalDateTime(2013, 1, 30, 14, 0));

		talk1 = TalkForTests.withDefaults(
				new LocalDateTime(2013, 1, 30, 16, 0), new LocalDateTime(2013,
						1, 30, 18, 0));
		talk2 = TalkForTests.withDefaults(
				new LocalDateTime(2013, 1, 30, 18, 0), end);

		expectedMeeting1.addTalk(talk1);
		expectedMeeting1.addTalk(talk2);

		dbSeeder.seedData(expectedMeeting1);
	}

	@Test
	public void shouldBeAbleToGetMeeting() {
		MeetingTO actualMeeting = meetingService
				.getMeeting(expectedMeeting1.getId());

		assertMeetingTO(actualMeeting, expectedMeeting1);
	}

	@Test
	public void shouldBeAbleToGetMeetings() {
		dbSeeder.seedData(expectedMeeting2);
		List<MeetingTO> meetings = meetingService.getMeetings();
		
		assertThat(meetings).hasSize(2);
		assertMeetingTO(meetings.get(0), expectedMeeting1);
		assertMeetingTO(meetings.get(1), expectedMeeting2);
	}

	@Test
	public void shouldBeAbleToUpdateMeeting() {
		MeetingTO meetingTO = meetingService
				.getMeeting(expectedMeeting1.getId());
		String newTitle = "new Title";
		meetingTO.setTitle(newTitle);
		
		MeetingTO actualMeetingTO = meetingService.update(meetingTO);

		assertThat(actualMeetingTO).isEqualsToByComparingFields(meetingTO);
		
		Meeting meeting = meetingRepository.findOne(actualMeetingTO.getId());
		assertThat(meeting.getTitle()).isEqualTo(newTitle);
	}
	
	@Test
	public void shouldBeAbleToCreateMeeting() {
		MeetingTO meetingTO = new MeetingTO();
		meetingTO.setTitle("title");
		meetingTO.setVenue("venue");
		meetingTO.setEnd(end.plusMonths(2));
		meetingTO.setStart(start.plusMonths(2));
		
		MeetingTO actualMeetingTO = meetingService.create(meetingTO);
		
		assertThat(actualMeetingTO).isLenientEqualsToByIgnoringFields(meetingTO, "id");
		assertThat(actualMeetingTO.getId()).isNotNull().isGreaterThan(0L);
		
		Meeting meeting = meetingRepository.findOne(actualMeetingTO.getId());
		assertThat(meeting).isNotNull();
	}

}