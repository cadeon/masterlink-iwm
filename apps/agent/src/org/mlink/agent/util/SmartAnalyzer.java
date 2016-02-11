package org.mlink.agent.util;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.PorterStemFilter;
import java.io.Reader;

public class SmartAnalyzer extends StandardAnalyzer
{
    public TokenStream tokenStream(final String s, final Reader reader)
    {
        return new PorterStemFilter(super.tokenStream(s, reader));
    }
}