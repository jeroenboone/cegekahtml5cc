package be.cegeka.eventualizr.application;

import java.util.List;

import javax.annotation.Nullable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import be.cegeka.eventualizr.application.mapper.MeetingMapper;
import be.cegeka.eventualizr.application.to.MeetingTO;
import be.cegeka.eventualizr.application.to.TalkTO;
import be.cegeka.eventualizr.domain.Meeting;
import be.cegeka.eventualizr.domain.MeetingRepository;
import be.cegeka.eventualizr.domain.Talk;

import com.google.common.base.Function;
import com.google.common.collect.Lists;


@Service
@Transactional
public class MeetingService {
	
	@Autowired
	private MeetingRepository meetingRepository;
	@Autowired
	private MeetingMapper meetingMapper;
	
	@Transactional(readOnly=true)
	public MeetingTO getMeeting(Long id) {
		return meetingMapper.toTO(meetingRepository.findOne(id));
	}
	
	@Transactional(readOnly=true)
	public TalkTO getTalk(Long meetingId, Long talkId) {
		Meeting meeting = meetingRepository.findOne(meetingId);
		Talk talk = meeting.getTalk(talkId);
		return meetingMapper.toTO(talk);
	}
	
	@Transactional(readOnly=true)
	public List<TalkTO> getTalks(Long meetingId) {
		Meeting meeting = meetingRepository.findOne(meetingId);
		return Lists.transform(meeting.getTalks(), new Function<Talk, TalkTO>(){
			@Override
			public TalkTO apply(@Nullable Talk talk) {
				return meetingMapper.toTO(talk);
			}
			
		});
	}
	
	@Transactional(readOnly=true)
	public List<MeetingTO> getMeetings() {
		return  Lists.transform(meetingRepository.findAll(), new Function<Meeting, MeetingTO>(){
			@Override
			public MeetingTO apply(@Nullable Meeting meeting) {
				return meetingMapper.toTO(meeting);
			}
		});
	};
	
	public MeetingTO update(MeetingTO meetingTO){
		Meeting meeting = meetingRepository.findOne(meetingTO.getId());
		meetingMapper.mergeEntity(meeting, meetingTO);
		return meetingMapper.toTO(meetingRepository.save(meeting));
	}
	
	public MeetingTO create(MeetingTO meetingTO){
		Meeting meeting = meetingMapper.toEntity(meetingTO);
		return meetingMapper.toTO(meetingRepository.save(meeting));
	}

}
