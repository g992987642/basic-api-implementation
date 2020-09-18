package com.thoughtworks.rslist.config;

import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VotesRepository;
import com.thoughtworks.rslist.service.RsService;
import com.thoughtworks.rslist.service.impl.RsServiceImpl;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class Configuration {

    private final UserRepository userRepository;
    private final RsEventRepository rsEventRepository;
    private final VotesRepository votesRepository;

    public Configuration(UserRepository userRepository, RsEventRepository rsEventRepository, VotesRepository votesRepository) {
        this.userRepository = userRepository;
        this.rsEventRepository = rsEventRepository;
        this.votesRepository = votesRepository;
    }

    @Bean(name = "rsService")
    public RsService getRsService() {
       return  new RsServiceImpl(userRepository,rsEventRepository,votesRepository);
    }


}
