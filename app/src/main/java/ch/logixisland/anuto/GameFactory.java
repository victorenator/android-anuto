package ch.logixisland.anuto;

import android.content.Context;

import ch.logixisland.anuto.business.control.BackButtonControl;
import ch.logixisland.anuto.business.control.TowerControl;
import ch.logixisland.anuto.business.control.TowerInserter;
import ch.logixisland.anuto.business.control.TowerSelector;
import ch.logixisland.anuto.business.game.GameSpeed;
import ch.logixisland.anuto.business.game.GameState;
import ch.logixisland.anuto.business.game.ScoreBoard;
import ch.logixisland.anuto.business.game.SettingsManager;
import ch.logixisland.anuto.business.level.HighScores;
import ch.logixisland.anuto.business.level.LevelLoader;
import ch.logixisland.anuto.business.level.LevelRepository;
import ch.logixisland.anuto.business.level.TowerAging;
import ch.logixisland.anuto.business.level.WaveManager;
import ch.logixisland.anuto.engine.logic.EntityStore;
import ch.logixisland.anuto.engine.logic.FrameRateLogger;
import ch.logixisland.anuto.engine.logic.GameEngine;
import ch.logixisland.anuto.engine.logic.GameLoop;
import ch.logixisland.anuto.engine.logic.MessageQueue;
import ch.logixisland.anuto.engine.render.Renderer;
import ch.logixisland.anuto.engine.render.Viewport;
import ch.logixisland.anuto.engine.render.sprite.SpriteFactory;
import ch.logixisland.anuto.engine.sound.SoundFactory;
import ch.logixisland.anuto.engine.sound.SoundManager;
import ch.logixisland.anuto.engine.theme.ThemeManager;
import ch.logixisland.anuto.entity.enemy.EnemyFactory;
import ch.logixisland.anuto.entity.plateau.PlateauFactory;
import ch.logixisland.anuto.entity.tower.TowerFactory;

public class GameFactory {

    // Engine
    private final ThemeManager mThemeManager;
    private final SoundManager mSoundManager;
    private final SpriteFactory mSpriteFactory;
    private final SoundFactory mSoundFactory;
    private final Viewport mViewport;
    private final FrameRateLogger mFrameRateLogger;
    private final EntityStore mEntityStore;
    private final MessageQueue mMessageQueue;
    private final Renderer mRenderer;
    private final GameEngine mGameEngine;
    private final GameLoop mGameLoop;

    // Entity
    private final PlateauFactory mPlateauFactory;
    private final TowerFactory mTowerFactory;
    private final EnemyFactory mEnemyFactory;

    // Business
    private final ScoreBoard mScoreBoard;
    private final HighScores mHighScores;
    private final TowerSelector mTowerSelector;
    private final TowerControl mTowerControl;
    private final TowerAging mTowerAging;
    private final TowerInserter mTowerInserter;
    private final LevelRepository mLevelRepository;
    private final LevelLoader mLevelLoader;
    private final WaveManager mWaveManager;
    private final GameSpeed mSpeedManager;
    private final GameState mGameState;
    private final SettingsManager mSettingsManager;
    private final BackButtonControl mBackButtonControl;

    public GameFactory(Context context) {
        // Engine
        mThemeManager = new ThemeManager(context);
        mSoundManager = new SoundManager(context);
        mSpriteFactory = new SpriteFactory(context, mThemeManager);
        mSoundFactory = new SoundFactory(context, mSoundManager);
        mViewport = new Viewport();
        mFrameRateLogger = new FrameRateLogger();
        mEntityStore = new EntityStore();
        mMessageQueue = new MessageQueue();
        mRenderer = new Renderer(mViewport, mThemeManager, mFrameRateLogger);
        mGameLoop = new GameLoop(mRenderer, mFrameRateLogger);
        mGameEngine = new GameEngine(mSpriteFactory, mThemeManager, mSoundFactory, mEntityStore, mMessageQueue, mRenderer, mGameLoop);

        // Entity
        mPlateauFactory = new PlateauFactory(mGameEngine);
        mTowerFactory = new TowerFactory(mGameEngine);
        mEnemyFactory = new EnemyFactory(mGameEngine);

        // Business
        mScoreBoard = new ScoreBoard();
        mLevelRepository = new LevelRepository();
        mSpeedManager = new GameSpeed(mGameEngine);
        mGameState = new GameState(mGameEngine, mThemeManager, mScoreBoard);
        mLevelLoader = new LevelLoader(context, mGameEngine, mScoreBoard, mGameState, mViewport, mPlateauFactory, mTowerFactory, mEnemyFactory);
        mLevelLoader.loadLevel(mLevelRepository.getLevels().get(0));
        mTowerAging = new TowerAging(mGameEngine, mLevelLoader);
        mWaveManager = new WaveManager(mGameEngine, mScoreBoard, mGameState, mLevelLoader, mEnemyFactory, mTowerAging);
        mHighScores = new HighScores(context, mGameState, mScoreBoard, mLevelLoader);
        mTowerSelector = new TowerSelector(mGameEngine, mGameState, mScoreBoard);
        mTowerControl = new TowerControl(mGameEngine, mScoreBoard, mTowerSelector, mTowerFactory);
        mTowerInserter = new TowerInserter(mGameEngine, mGameState, mTowerFactory, mTowerSelector, mTowerAging, mScoreBoard);
        mSettingsManager = new SettingsManager(context, mThemeManager, mSoundManager);
        mBackButtonControl = new BackButtonControl(mSettingsManager);

        mGameState.restart();
    }

    public ThemeManager getThemeManager() {
        return mThemeManager;
    }

    public Viewport getViewport() {
        return mViewport;
    }

    public Renderer getRenderer() {
        return mRenderer;
    }

    public GameEngine getGameEngine() {
        return mGameEngine;
    }

    public TowerFactory getTowerFactory() {
        return mTowerFactory;
    }

    public ScoreBoard getScoreBoard() {
        return mScoreBoard;
    }

    public TowerSelector getTowerSelector() {
        return mTowerSelector;
    }

    public TowerControl getTowerControl() {
        return mTowerControl;
    }

    public TowerInserter getTowerInserter() {
        return mTowerInserter;
    }

    public LevelLoader getLevelLoader() {
        return mLevelLoader;
    }

    public WaveManager getWaveManager() {
        return mWaveManager;
    }

    public GameSpeed getSpeedManager() {
        return mSpeedManager;
    }

    public GameState getGameState() {
        return mGameState;
    }

    public SettingsManager getSettingsManager() {
        return mSettingsManager;
    }

    public BackButtonControl getBackButtonControl() {
        return mBackButtonControl;
    }

    public LevelRepository getLevelRepository() {
        return mLevelRepository;
    }

    public HighScores getHighScores() {
        return mHighScores;
    }

}
