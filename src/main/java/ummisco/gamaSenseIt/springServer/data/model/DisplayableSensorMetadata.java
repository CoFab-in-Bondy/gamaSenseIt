package ummisco.gamaSenseIt.springServer.data.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DisplayableSensorMetadata {
  private Long idType;
  private String name;
  private String version;
  private String dataSeparator;
  private String measuredDataOrder;
  private List<String> parameterMetadata;
  private String description;

  public DisplayableSensorMetadata(Long idType, String name, String version, String dataSeparator,
      String measuredDataOrder, Set<ParameterMetadata> parameterMetadata, String description) {
    super();
    this.idType = idType;
    this.name = name;
    this.version = version;
    this.dataSeparator = dataSeparator;
    this.measuredDataOrder = measuredDataOrder;
    this.parameterMetadata = new ArrayList<>();
    for (ParameterMetadata pm : parameterMetadata) {
      this.parameterMetadata.add(pm.getVarName());
    }
    this.description = description;
  }

  public DisplayableSensorMetadata(SensorMetadata s) {
    this(s.getIdType(), s.getName(), s.getVersion(), s.getDataSeparator(), s.getMeasuredDataOrder(),
        s.getParameterMetadata(), s.getDescription());
  }

  public Long getIdType() {
    return idType;
  }

  public void setIdType(Long idType) {
    this.idType = idType;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getMeasuredDataOrder() {
    return measuredDataOrder;
  }

  public void setMeasuredDataOrder(String measuredDataOrder) {
    this.measuredDataOrder = measuredDataOrder;
  }

  public String getDataSeparator() {
    return dataSeparator;
  }

  public void setDataSeparator(String dataSeparator) {
    this.dataSeparator = dataSeparator;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

}
