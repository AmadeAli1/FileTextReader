package annotations;

/**
 * @Author <h2>Amade Ali</h2>
 * This Enum contains all data types supported in the text file
 */
public enum DataType {
    BOOLEAN {
        @Override
        public Object convert(String value) {
            return Boolean.parseBoolean(value);
        }
    },
    BYTE {
        @Override
        public Object convert(String value) {
            return Byte.parseByte(value);
        }
    },
    DOUBLE {
        @Override
        public Object convert(String value) {
            return Double.parseDouble(value);
        }
    },
    FLOAT {
        @Override
        public Object convert(String value) {
            return Float.parseFloat(value);
        }
    },
    INTEGER {
        @Override
        public Object convert(String value) {
            return Integer.parseInt(value);
        }
    },
    LONG {
        @Override
        public Object convert(String value) {
            return Long.parseLong(value);
        }
    },
    SHORT {
        @Override
        public Object convert(String value) {
            return Short.parseShort(value);
        }
    },
    STRING {
        @Override
        public Object convert(String value) {
            return value;
        }
    };


    /**
     * Converts a String to the specific data type used in the class
     * @param value Token
     * @return Correct data type
     */
    abstract public Object convert(String value);
}
