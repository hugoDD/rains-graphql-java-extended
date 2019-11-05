package com.extended.graphql.boot;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author <a href='mailto:280555235@qq.com'>hugoDD</a>
 */
@ConfigurationProperties(prefix = "graphql.datetime.scalars")
public class GraphQLExtendedProperties {

    private ScalarTypeDefinition object = new ScalarTypeDefinition();
    private ScalarDateDefinition date = new ScalarDateDefinition();
    private ScalarDateDefinition localDate = new ScalarDateDefinition();
    private ScalarDateDefinition localDateTime = new ScalarDateDefinition();
    private ScalarDateDefinition localTime = new ScalarDateDefinition();
    private ScalarDateDefinition offsetDateTime = new ScalarDateDefinition();
    private boolean zoneConversionEnabled = false;

    public ScalarTypeDefinition getObject() {
        return object;
    }

    public void setObject(ScalarTypeDefinition object) {
        this.object = object;
    }

    ScalarDateDefinition getDate() {
        return date;
    }

    public void setDate(ScalarDateDefinition date) {
        this.date = date;
    }

    ScalarDateDefinition getLocalDate() {
        return localDate;
    }

    public void setLocalDate(ScalarDateDefinition localDate) {
        this.localDate = localDate;
    }

    ScalarDateDefinition getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(ScalarDateDefinition localDateTime) {
        this.localDateTime = localDateTime;
    }

    ScalarDateDefinition getLocalTime() {
        return localTime;
    }

    public void setLocalTime(ScalarDateDefinition localTime) {
        this.localTime = localTime;
    }

    ScalarDateDefinition getOffsetDateTime() {
        return offsetDateTime;
    }

    public void setOffsetDateTime(ScalarDateDefinition offsetDateTime) {
        this.offsetDateTime = offsetDateTime;
    }

    public boolean isZoneConversionEnabled() {
        return zoneConversionEnabled;
    }

    public void setZoneConversionEnabled(boolean zoneConversionEnabled) {
        this.zoneConversionEnabled = zoneConversionEnabled;
    }

    public static class ScalarTypeDefinition {
        private String scalarName;
        private String description;

        public String getScalarName() {
            return scalarName;
        }

        public void setScalarName(String scalarName) {
            this.scalarName = scalarName;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    public static class ScalarDateDefinition extends ScalarTypeDefinition {

        private String scalarName;

        private String pattern;

        public String getScalarName() {
            return scalarName;
        }

        public void setScalarName(String scalarName) {
            this.scalarName = scalarName;
        }

        public String getPattern() {
            return pattern;
        }

        public void setPattern(String pattern) {
            this.pattern = pattern;
        }
    }
}
