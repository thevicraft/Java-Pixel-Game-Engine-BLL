package com.javapixelgame.game.resourcehandling;

import java.util.ArrayList;
import java.util.List;

import com.javapixelgame.game.api.graphics.Texture;

public class TextureLoader {

	private static List<Texture> textures = new ArrayList<>();
	private static final Texture unknown = new Texture(TextureID.WARNING);

	public static void loadAll() {

		for (TextureID id : TextureID.values()) {
			textures.add(new Texture(id));
		}
		for (AnimatedTextureID id : AnimatedTextureID.values()) {
			textures.add(new Texture(id, ""));
		}

	}
	
	public static void unloadAll() {
		textures.clear();
		textures = new ArrayList<>();
		System.gc();
	}

	public static Texture get(AnimatedTextureID id) {
		for (Texture t : textures.toArray(new Texture[0])) {
			if (t.get_animated_id() != null && t.get_animated_id().equals(id))
				return t;
		}
		return unknown;

	}

	public static Texture get(TextureID id) {
		for (Texture t : textures.toArray(new Texture[0])) {
			if (t.get_unanimated_id() != null && t.get_unanimated_id().equals(id))
				return t;
		}
		return unknown;

	}

}
