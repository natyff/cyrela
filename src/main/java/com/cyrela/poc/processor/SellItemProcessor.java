package com.cyrela.poc.processor;

import com.cyrela.poc.domain.Sell;
import org.springframework.batch.item.ItemProcessor;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

public class SellItemProcessor implements ItemProcessor<Sell, Sell> {

    static public List<String> exit = new ArrayList<>();

    @Override
    public Sell process(Sell sell) throws Exception {
        if(sell.getRegional().isEmpty()){
            return null;
        }
        String s = Normalizer.normalize(sell.getRegional(), Normalizer.Form.NFD);
        s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        exit.add(s+ ",");
        exit.add(sell.getvGV().replace(",", "."));
        exit.add("\n");
        return null;
    }
}
