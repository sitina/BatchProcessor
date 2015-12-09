package net.sitina.bp.api;

public interface Hub {

    void setComplete();

    boolean isComplete();

    String getItem();

    void putItem(String item);

}
