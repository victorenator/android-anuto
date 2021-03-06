package ch.logixisland.anuto.entity.effect;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import ch.logixisland.anuto.engine.logic.Entity;
import ch.logixisland.anuto.engine.logic.EntityListener;
import ch.logixisland.anuto.engine.logic.GameEngine;
import ch.logixisland.anuto.engine.logic.TickListener;
import ch.logixisland.anuto.engine.render.Drawable;
import ch.logixisland.anuto.engine.render.Layers;
import ch.logixisland.anuto.util.math.Function;
import ch.logixisland.anuto.util.math.SampledFunction;

public class TeleportedMarker extends Effect implements EntityListener {

    private static final float MARKER_MIN_RADIUS = 0.1f;
    private static final float MARKER_MAX_RADIUS = 0.2f;
    private static final float MARKER_SPEED = 1f;

    private class StaticData implements TickListener {
        private SampledFunction mScaleFunction;

        @Override
        public void tick() {
            mScaleFunction.step();
        }
    }

    private class MarkerDrawable implements Drawable {
        private Paint mPaint;

        private MarkerDrawable() {
            mPaint = new Paint();
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(Color.MAGENTA);
            mPaint.setAlpha(30);
        }

        @Override
        public int getLayer() {
            return Layers.SHOT;
        }

        @Override
        public void draw(Canvas canvas) {
            canvas.drawCircle(
                    getPosition().x(),
                    getPosition().y(),
                    mStaticData.mScaleFunction.getValue(),
                    mPaint);
        }
    }

    private final Entity mMarked;
    private StaticData mStaticData;
    private MarkerDrawable mDrawable;

    public TeleportedMarker(Entity origin, Entity marked) {
        super(origin);
        mMarked = marked;
        mMarked.addListener(this);

        mDrawable = new MarkerDrawable();
    }

    @Override
    public Object initStatic() {
        StaticData s = new StaticData();

        s.mScaleFunction = Function.sine()
                .multiply((MARKER_MAX_RADIUS - MARKER_MIN_RADIUS) / 2)
                .offset((MARKER_MAX_RADIUS + MARKER_MIN_RADIUS) / 2)
                .stretch(GameEngine.TARGET_FRAME_RATE / MARKER_SPEED / (float) Math.PI)
                .sample();

        getGameEngine().add(s);
        return s;
    }

    @Override
    public void init() {
        super.init();

        mStaticData = (StaticData) getStaticData();
        getGameEngine().add(mDrawable);
    }

    @Override
    public void clean() {
        super.clean();

        getGameEngine().remove(mDrawable);
    }

    @Override
    public void tick() {
        super.tick();

        setPosition(mMarked.getPosition());
    }

    @Override
    public void entityRemoved(Entity obj) {
        remove();
    }
}
