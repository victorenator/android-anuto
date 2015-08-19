package ch.logixisland.anuto.game.objects.impl;

import android.graphics.Canvas;

import ch.logixisland.anuto.R;
import ch.logixisland.anuto.game.GameEngine;
import ch.logixisland.anuto.game.Layers;
import ch.logixisland.anuto.game.objects.Shot;
import ch.logixisland.anuto.game.objects.Sprite;
import ch.logixisland.anuto.util.math.ParabolaFunction;
import ch.logixisland.anuto.util.math.Vector2;

public class MortarShot extends Shot {

    public final static float EXPLOSION_RADIUS = 1.5f;
    public final static float TIME_TO_TARGET = 1.5f;

    private final static float HEIGHT_SCALING_START = 0.5f;
    private final static float HEIGHT_SCALING_STOP = 1.0f;
    private final static float HEIGHT_SCALING_PEAK = 1.5f;

    private float mDamage;
    private float mAngle;
    private ParabolaFunction mHeightScalingFunction;

    private final Sprite mSprite;

    public MortarShot(Vector2 position, Vector2 target, float damage) {
        setPosition(position);

        mSprite = Sprite.fromResources(mGame.getResources(), R.drawable.grenade, 4);
        mSprite.setListener(this);
        mSprite.setIndex(mGame.getRandom().nextInt(4));
        mSprite.setMatrix(0.7f, 0.7f, null, null);
        mSprite.setLayer(Layers.SHOT);

        mHeightScalingFunction = new ParabolaFunction();
        mHeightScalingFunction.setProperties(HEIGHT_SCALING_START, HEIGHT_SCALING_STOP, HEIGHT_SCALING_PEAK);
        mHeightScalingFunction.setSection(GameEngine.TARGET_FRAME_RATE * TIME_TO_TARGET);

        mSpeed = getDistanceTo(target) / TIME_TO_TARGET;
        mDirection = getDirectionTo(target);
        mDamage = damage;
    }

    @Override
    public void init() {
        super.init();

        mGame.add(mSprite);

        mAngle = mGame.getRandom(360f);
    }

    @Override
    public void clean() {
        super.clean();

        mGame.remove(mSprite);
    }

    @Override
    public void onDraw(Sprite sprite, Canvas canvas) {
        super.onDraw(sprite, canvas);

        float s = mHeightScalingFunction.getValue();
        canvas.scale(s, s);
        canvas.rotate(mAngle);
    }

    @Override
    public void tick() {
        super.tick();

        if (mHeightScalingFunction.step()) {
            mGame.add(new Explosion(mPosition, mDamage, EXPLOSION_RADIUS));
            this.remove();
        }
    }
}
