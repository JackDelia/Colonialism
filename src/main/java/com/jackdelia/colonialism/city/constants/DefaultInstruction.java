package com.jackdelia.colonialism.city.constants;

/**
 * City Defaults
 */
public enum DefaultInstruction {
    STOCKPILE {
        public double getDefaultValue() {
            return 10.0;
        }
        public String toString(){
            return "stockpile";
        }
    },
    RETURN {
        public double getDefaultValue() {
            return 0.0;
        }
        public String toString(){
            return "return";
        }
    },
    SELL {
        public double getDefaultValue() {
            return 100.0;
        }
        public String toString(){
            return "sell";
        }
    },
    EXPORT {
        public double getDefaultValue() {
            return 0.0;
        }
        public String toString(){
            return "export";
        }
    };

    public abstract double getDefaultValue();
    public abstract String toString();
}