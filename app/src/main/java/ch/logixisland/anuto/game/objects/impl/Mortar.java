package ch.logixisland.anuto.game.objects.impl;

import android.graphics.Canvas;

import ch.logixisland.anuto.R;
import ch.logixisland.anuto.game.Layers;
import ch.logixisland.anuto.game.objects.AimingTower;
import ch.logixisland.anuto.game.objects.Sprite;
import ch.logixisland.anuto.util.math.Vector2;

public class Mortar extends AimingTower {

    private final static float INACCURACY = 1.0f;

    private final static float SHOT_SPAWN_OFFSET = 0.6f;
    private final static float REBOUND_DURATION = 0.5f;

    private float mAngle;
    private boolean mRebounding;

    private Sprite mSpriteBase;
    private Sprite mSpriteCanon;

    private Sprite.Animator mAnimator;

    public Mortar() {
        mAngle = 90f;

        mSpriteBase = Sprite.fromResources(mGame.getResources(), R.drawable.base2, 4);
        mSpriteBase.setListener(this);
        mSpriteBase.setIndex(mGame.getRandom().nextInt(4));
        mSpriteBase.setMatrix(1f, 1f, null, null);
        mSpriteBase.setLayer(Layers.TOWER_BASE);

        mSpriteCanon = Sprite.fromResources(mGame.getResources(), R.drawable.mortar, 8);
        mSpriteCanon.setListener(this);
        mSpriteCanon.setMatrix(0.8f, null, new Vector2(0.4f, 0.2f), -90f);
        mSpriteCanon.setLayer(Layers.TOWER);

        mAnimator = new Sprite.Animator();
        mAnimator.setSequence(mSpriteCanon.sequenceForwardBackward());
        mAnimator.setInterval(REBOUND_DURATION);
        mSpriteCanon.setAnimator(mAnimator);
    }

    @Override
    public void init() {
        super.init();

        mGame.add(mSpriteBase);
        mGame.add(mSpriteCanon);
    }

    @Override
    public void clean() {
        super.clean();

        mGame.remove(mSpriteBase);
        mGame.remove(mSpriteCanon);
    }

    @Override
    public void onDraw(Sprite sprite, Canvas canvas) {
        super.onDraw(sprite, canvas);

        if (sprite == mSpriteCanon) {
            canvas.rotate(mAngle);
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (mTarget != null && mReloaded) {
            Vector2 targetPos = mTarget.getPositionAfter(Mine.TIME_TO_TARGET);
            targetPos.add(Vector2.polar(mGame.getRandom(INACCURACY), mGame.getRandom(360f)));
            mAngle = getAngleTo(targetPos);

            Vector2 shotPos = mPosition.copy().add(Vector2.polar(SHOT_SPAWN_OFFSET, mAngle));

            mGame.add(new MortarShot(shotPos, targetPos, mConfig.damage));

            mReloaded = false;
            mRebounding = true;
        }

        if (mRebounding && mSpriteCanon.animate()) {
            mRebounding = false;
        }
    }

    @Override
    public void drawPreview(Canvas canvas) {
        mSpriteBase.draw(canvas);
        mSpriteCanon.draw(canvas);
    }
}
