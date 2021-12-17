package com.ruho.rsk.domain;

import java.io.Serializable;
import java.util.List;

public class RskDecodedData implements Serializable {

    private static final long serialVersionUID = 8983171452957536766L;

    private String name;
    private String signature;
    private List<Param> params;

    public static class Param {
        String name;
        String type;
        Boolean indexed;
        Boolean decoded;
        Object value;

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }

        public Boolean getIndexed() {
            return indexed;
        }

        public Boolean getDecoded() {
            return decoded;
        }

        public Object getValue() {
            return value;
        }
    }

    public String getName() {
        return name;
    }

    public String getSignature() {
        return signature;
    }

    public List<Param> getParams() {
        return params;
    }
}
