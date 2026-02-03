package local.nca.callcenter.asterisk.application.service;

import local.nca.callcenter.asterisk.infrastructure.AsteriskConnection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
// ... остальные импорты

@Slf4j
@Service
@RequiredArgsConstructor
public class OriginateService {

    private final AsteriskConnection asteriskConnection;

    public boolean isConnected() {
        return asteriskConnection.isConnected();
    }

}