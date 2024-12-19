package uwu.lopyluna.obelisk.helper.provider;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

import java.util.Map;

import static uwu.lopyluna.obelisk.Obelisk.MOD_ID;

public class DataGeneratorLangProvider extends LanguageProvider {

    private final Map<String, String> langEntries;

    public DataGeneratorLangProvider(PackOutput output, Map<String, String> langEntries) {
        super(output, MOD_ID, "en_us");
        this.langEntries = langEntries;
    }

    @Override
    protected void addTranslations() {
        langEntries.forEach(this::add);
    }
}
