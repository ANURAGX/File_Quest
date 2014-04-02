package org.anurag.inherited.sony;

/**
 * A very simple dynamics implementation with spring-like behavior
 */
public class SimpleDynamics extends Dynamics {

    /** The friction factor */
    private float mFrictionFactor;

    /** The snap to factor */
    private float mSnapToFactor;

    /**
     * Creates a SimpleDynamics object
     * 
     * @param frictionFactor The friction factor. Should be between 0 and 1.
     *            A higher number means a slower dissipating speed.
     * @param snapToFactor The snap to factor. Should be between 0 and 1. A
     *            higher number means a stronger snap.
     */
    public SimpleDynamics(final float frictionFactor, final float snapToFactor) {
        mFrictionFactor = frictionFactor;
        mSnapToFactor = snapToFactor;
    }

    @Override
    protected void onUpdate(final int dt) {
        // update the velocity based on how far we are from the snap point
        mVelocity += getDistanceToLimit() * mSnapToFactor;

        // then update the position based on the current velocity
        mPosition += mVelocity * dt / 1000;

        // and finally, apply some friction to slow it down
        mVelocity *= mFrictionFactor;
    }
}
