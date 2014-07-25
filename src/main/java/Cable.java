public class Cable {
    private int puid;
    private String type;
    private double length;
    private String color;
    private String network;
    private String fabric;

    public void setPuid(int puid) {
        this.puid = puid;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public void setFabric(String fabric) {
        this.fabric = fabric;
    }

    public int getPuid() {
        return puid;
    }

    public String getType() {
        return type;
    }

    public double getlength() {
        return length;
    }

    public String getColor() {
        return color;
    }

    public String getNetwork() {
        return network;
    }

    public String getFabric() {
        return fabric;
    }
}
