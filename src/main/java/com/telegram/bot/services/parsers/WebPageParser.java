package com.telegram.bot.services.parsers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * This service parses the page of the romanian ministry of justice (link should be specified in properties) and returns
 * their lists, which are containing information about the actual state of a dosser.
 *
 * This parser is needed as the romanian ministry of justice refused to offer me open API to obtain their lists.
 */
@Service
public class WebPageParser {

    private final String ministryDossierUrl;

    private final String webParserYear;

    //This flag allows to resolve links using absolute path instead of relative
    private static final String A_TAG_RESOLVER = "abs:href";

    @Autowired
    public WebPageParser(@Value("${general.ministry.dossier.url}") String ministryDossierUrl,
                         @Value("${settings.web.parser.year}") String webParserYear) {
        this.ministryDossierUrl = ministryDossierUrl;
        this.webParserYear = webParserYear;
    }

    public List<String> gePdfLinks() {
        final String regexPdfForSpecifiedYear = "a[href~=/images/.+" + webParserYear + ".*\\.pdf$]";

        try {
            final Document doc = Jsoup.connect(ministryDossierUrl).get();
            Elements elements = doc.select(regexPdfForSpecifiedYear);
            return elements.stream().map(link -> link.attr(A_TAG_RESOLVER)).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }
}
