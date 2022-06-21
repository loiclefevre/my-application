package com.mycompany.sample.javafx;

import javafx.animation.Interpolator;
import javafx.animation.Timeline;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.util.Duration;

public class CachedTimelineTransition extends MobileTransition {
	protected static final Interpolator WEB_EASE = Interpolator.SPLINE(0.25D, 0.1D, 0.25D, 1.0D);
	protected final Node node;
	protected Timeline timeline;
	private boolean oldCache;
	private CacheHint oldCacheHint;
	private final boolean useCache;
	private final boolean opacityInterpolated;

	public CachedTimelineTransition(Node node, Timeline timeline) {
		this(node, timeline, true);
	}

	public CachedTimelineTransition(Node node, Timeline timeline, boolean useCache) {
		this(node, timeline, useCache, false);
	}

	public CachedTimelineTransition(Node node, Timeline timeline, boolean useCache, boolean opacityInterpolated) {
		this.oldCache = false;
		this.oldCacheHint = CacheHint.DEFAULT;
		this.node = node;
		this.timeline = timeline;
		this.useCache = useCache;
		this.opacityInterpolated = opacityInterpolated;
		if (this instanceof HideableTransition) {
			this.hideNode();
		}

	}

	protected void starting() {
		super.starting();
		if (this.useCache) {
			this.oldCache = this.node.isCache();
			this.oldCacheHint = this.node.getCacheHint();
			this.node.setCache(true);
			this.node.setCacheHint(CacheHint.SPEED);
		}

	}

	protected void stopping() {
		super.stopping();
		if (this.useCache) {
			this.node.setCache(this.oldCache);
			this.node.setCacheHint(this.oldCacheHint);
		}

	}

	public void pause() {
		if (this.timeline != null) {
			this.timeline.pause();
		}

		super.pause();
	}

	protected void interpolate(double d) {
		if (this.timeline != null) {
			this.timeline.playFrom(Duration.seconds(d));
			this.timeline.stop();
		}

	}

	public final void hideNode() {
		if (this.node != null) {
			this.node.setOpacity(0.0D);
		}

	}

	protected boolean isOpacityInterpolated() {
		return this.opacityInterpolated;
	}
}
