package view.plan;

import model.Intersection;

public interface ClickablePOI {
    public void updateTrack(Intersection origin, int radiusOrigin, Intersection destination, int radiusDestination);
}