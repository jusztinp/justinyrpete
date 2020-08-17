package com.justiny.rpete.service;

import com.justiny.rpete.dto.SubredditDto;
import com.justiny.rpete.exceptions.RpeteException;
import com.justiny.rpete.mapper.SubredditMapper;
import com.justiny.rpete.model.Subreddit;
import com.justiny.rpete.repository.SubredditRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class SubredditService {
    SubredditRepository subredditRepository;
    SubredditMapper subredditMapper;

    @Transactional
    public SubredditDto save(SubredditDto subredditDto) {
        Subreddit subreddit = subredditRepository.save(subredditMapper.mapDtoToSubreddit(subredditDto));
        subredditDto.setId(subreddit.getId());
        return subredditDto;
    }


    @Transactional(readOnly = true)
    public List<SubredditDto> getAll() {
        return subredditRepository.findAll()
                .stream()
                .map(subredditMapper::mapSubredditToDto)
                .collect(Collectors.toList());

    }

    public SubredditDto getSubredditById(Long id) {
        Subreddit subreddit = subredditRepository.findById(id).orElseThrow(() -> new RpeteException("No subreddit found with id " + id));
        return subredditMapper.mapSubredditToDto(subreddit);
    }
}
