package org.example;

public class Configuration {
    private final int totalTickets;
    private final int maxCapacity;
    private final int releaseRate;
    private final int retrievalRate;

    public Configuration(int totalTickets, int maxCapacity, int releaseRate, int retrievalRate) {
        this.totalTickets = totalTickets;
        this.maxCapacity = maxCapacity;
        this.releaseRate = releaseRate;
        this.retrievalRate = retrievalRate;
    }

    public int getTotalTickets() {
        return totalTickets;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public int getReleaseRate() {
        return releaseRate;
    }

    public int getRetrievalRate() {
        return retrievalRate;
    }
}
