package example.vbc.convert.converter;

import org.springframework.core.convert.ConversionService;

public class ConversionServiceMain {

    public static void main(String[] args) {

    }

    private class ConversionServiceTest{

        final ConversionService conversionService;

        ConversionServiceTest(ConversionService conversionService){
            this.conversionService = conversionService;
        }

    }
}
