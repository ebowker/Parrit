package com.parrit.transformers;

import com.parrit.DTOs.PairingHistoryDTO;
import com.parrit.entities.PairingHistory;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

public class PairingHistoryTransformer {

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    static {
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    public static PairingHistoryDTO transform(PairingHistory pairingHistory) {
        PairingHistoryDTO pairingHistoryDTO = new PairingHistoryDTO();
        pairingHistoryDTO.setPairingTime(simpleDateFormat.format(pairingHistory.getTimestamp()));
        pairingHistoryDTO.setPairingBoardName(pairingHistory.getPairingBoardName());
        pairingHistoryDTO.setPeople(PersonTransformer.transform(pairingHistory.getPeople()));
        return pairingHistoryDTO;
    }

    public static List<PairingHistoryDTO> transform(List<PairingHistory> pairingHistories) {
        if(pairingHistories == null || pairingHistories.isEmpty()) return Collections.emptyList();
        return pairingHistories.stream()
            .map(PairingHistoryTransformer::transform)
            .collect(Collectors.toList());
    }

}
