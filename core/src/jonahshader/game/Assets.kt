package jonahshader.game

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader

//    private final FreeTypeFontGenerator.FreeTypeFontParameter hudFontParams = new FreeTypeFontGenerator.FreeTypeFontParameter();

class Assets {

    val manager = AssetManager()

    //Spritesheets
    val itemAtlas = "Spritesheets/non_tile_spritesheet.pack"
    //Sounds
    val fuse = "audio/sounds/wick light.mp3"
    val explosion = "audio/sounds/explosion.mp3"
    val death = "audio/sounds/death.mp3"

    val menuHover = "audio/sounds/menu_tick_over.ogg"
    val menuClick = "audio/sounds/menu_tick_in.ogg"
    val menuRelease = "audio/sounds/menu_tick_out.ogg"

    //Fonts
    val loadingFont = "Fonts/visitor1.ttf"

    fun load() {
        manager.load(itemAtlas, TextureAtlas::class.java)
        manager.load(fuse, Sound::class.java)
        manager.load(explosion, Sound::class.java)
        manager.load(death, Sound::class.java)

        manager.load(menuHover, Sound::class.java)
        manager.load(menuClick, Sound::class.java)
        manager.load(menuRelease, Sound::class.java)

        //loading TFFs

        //loadint hudFont
        val resolver = InternalFileHandleResolver()
        manager.setLoader<FreeTypeFontGenerator, FreeTypeFontGeneratorLoader.FreeTypeFontGeneratorParameters>(FreeTypeFontGenerator::class.java, FreeTypeFontGeneratorLoader(resolver))
        manager.setLoader<BitmapFont, FreetypeFontLoader.FreeTypeFontLoaderParameter>(BitmapFont::class.java, ".ttf", FreetypeFontLoader(resolver))
        val hudFont = FreetypeFontLoader.FreeTypeFontLoaderParameter()
        hudFont.fontFileName = loadingFont
        hudFont.fontParameters.size = 36
        manager.load(loadingFont, BitmapFont::class.java, hudFont)
    }

    fun dispose() {
        manager.dispose()
    }
}//        hudFontParams.size = 50;
