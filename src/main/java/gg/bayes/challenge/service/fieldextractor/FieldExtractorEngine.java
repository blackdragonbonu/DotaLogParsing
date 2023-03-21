package gg.bayes.challenge.service.fieldextractor;

import gg.bayes.challenge.service.fieldextractor.steps.FieldExtractor;

import java.util.ArrayList;
import java.util.List;

/**
 * Field Extraction engine is used by parser classes to extract fields
 * Do not explicitly create instance of this class but instead make use of
 * the engine factory to create instances
 * @param <T>
 */
public class FieldExtractorEngine<T> {
    private final List<FieldExtractor<T>> steps;

    private FieldExtractorEngine(List<FieldExtractor<T>> steps){
        this.steps = steps;
    }

    /**
     * Method to be called to extract fields by making use of the engine
     * @param input Retrieve fields from the input
     */
    public void extractFields(T input){
        for(FieldExtractor<T> step : steps){
            try {
                step.run(input);
            }
            catch (Exception ignored){}
        }
    }

    /**
     * Builder class to create an instance of FieldExtraction Engine
     * To be used within a FieldExtractionEngine factory
     * @param <T>
     */
    public static class Builder<T>{
        private final List<FieldExtractor<T>> steps = new ArrayList<>();

        public Builder<T> addStep(FieldExtractor<T> step){
            this.steps.add(step);
            return this;
        }

        public FieldExtractorEngine<T> build(){
            if(this.steps.size() == 0)
                return null;
            return new FieldExtractorEngine<>(this.steps);
        }

    }
}
