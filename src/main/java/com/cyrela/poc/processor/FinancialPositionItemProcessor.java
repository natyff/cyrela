package com.cyrela.poc.processor;

import com.cyrela.poc.domain.FinancialPosition;
import org.springframework.batch.item.ItemProcessor;

import java.util.ArrayList;
import java.util.List;

public class FinancialPositionItemProcessor implements ItemProcessor<FinancialPosition, FinancialPosition> {

    static public List<String> exit = new ArrayList<>();

    @Override
    public FinancialPosition process(FinancialPosition financialPosition) throws Exception {

        exit.add(financialPosition.getObra() + ",");
        exit.add(financialPosition.getBloco() + ",");
        exit.add(financialPosition.getUnidade() + ",");
        exit.add(financialPosition.getDataVenda() + ",");
        exit.add(financialPosition.getFaseIncorporacao());
        exit.add("\n");

        return financialPosition;
    }
}
