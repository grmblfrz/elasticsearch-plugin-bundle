package org.xbib.elasticsearch.plugin.bundle.index.analysis.hyphen;

import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.IndexSettings;
import org.elasticsearch.index.analysis.CharFilterFactory;
import org.elasticsearch.index.analysis.CustomAnalyzer;
import org.elasticsearch.index.analysis.TokenFilterFactory;
import org.elasticsearch.index.analysis.TokenizerFactory;
import org.elasticsearch.index.analysis.XbibCustomAnalyzerProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A Hyphen analyzer provider.
 */
public class HyphenAnalyzerProvider extends XbibCustomAnalyzerProvider {

    private final Settings analyzerSettings;

    private final HyphenTokenizerFactory tokenizerFactory;

    private final HyphenTokenFilterFactory tokenFilterFactory;

    private CustomAnalyzer customAnalyzer;

    public HyphenAnalyzerProvider(IndexSettings indexSettings,
                                  Environment environment,
                                  String name,
                                  Settings settings) {
        super(indexSettings, name, settings);
        this.tokenizerFactory = new HyphenTokenizerFactory(indexSettings, environment, name, settings);
        this.tokenFilterFactory = new HyphenTokenFilterFactory(indexSettings, environment, name, settings);
        this.analyzerSettings = settings;
    }

    @Override
    protected void build(final Map<String, TokenizerFactory> tokenizers,
                      final Map<String, CharFilterFactory> charFilters,
                      final Map<String, TokenFilterFactory> tokenFilters) {
        List<CharFilterFactory> myCharFilters = new ArrayList<>();
        List<String> charFilterNames = analyzerSettings.getAsList("char_filter");
        for (String charFilterName : charFilterNames) {
            CharFilterFactory charFilter = charFilters.get(charFilterName);
            if (charFilter == null) {
                throw new IllegalArgumentException("hyphen analyzer [" + name()
                        + "] failed to find char_filter under name [" + charFilterName + "]");
            }
            myCharFilters.add(charFilter);
        }
        List<TokenFilterFactory> myTokenFilters = new ArrayList<>();
        myTokenFilters.add(tokenFilterFactory);
        List<String> tokenFilterNames = analyzerSettings.getAsList("filter");
        for (String tokenFilterName : tokenFilterNames) {
            TokenFilterFactory tokenFilter = tokenFilters.get(tokenFilterName);
            if (tokenFilter == null) {
                throw new IllegalArgumentException("hyphen analyzer [" + name()
                        + "] failed to find filter under name [" + tokenFilterName + "]");
            }
            myTokenFilters.add(tokenFilter);
        }
        int positionOffsetGap = analyzerSettings.getAsInt("position_offset_gap", 0);
        int offsetGap = analyzerSettings.getAsInt("offset_gap", -1);
        this.customAnalyzer = new CustomAnalyzer(tokenizerFactory,
                myCharFilters.toArray(new CharFilterFactory[0]),
                myTokenFilters.toArray(new TokenFilterFactory[0]),
                positionOffsetGap, offsetGap);
    }

    @Override
    public CustomAnalyzer get() {
        return this.customAnalyzer;
    }
}
