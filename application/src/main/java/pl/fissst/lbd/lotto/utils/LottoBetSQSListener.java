package pl.fissst.lbd.lotto.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.stereotype.Component;
import pl.fissst.lbd.lotto.dto.BetDto;
import pl.fissst.lbd.lotto.mapper.BetMapper;
import pl.fissst.lbd.lotto.model.BetModelApi;
import pl.fissst.lbd.lotto.service.BetService;

@Component
public class LottoBetSQSListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(LottoBetSQSListener.class);

    private BetMapper betMapper;
    private BetService betService;

    public LottoBetSQSListener(BetMapper betMapper, BetService betService) {
        this.betMapper = betMapper;
        this.betService = betService;
    }

    @SqsListener(value = "lotto.bet")
    public void onSQSBetApiMessage(BetModelApi betModelApi) {
        LOGGER.info("Invoked SQS lotto.bet listener");
        BetDto betDto = betMapper.mapModelApiToDto(betModelApi);
        betService.create(betDto);
    }
}