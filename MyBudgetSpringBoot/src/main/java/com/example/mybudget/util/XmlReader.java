package com.example.mybudget.util;

import com.example.mybudget.models.entities.AccountEntity;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class XmlReader {
    private static final String FILE = "my_budget_data.xml";

    public static List<AccountEntity> parseXMLData() throws IOException, JAXBException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream(FILE);

        if (is == null) {
            throw new IOException("File not found: " + FILE);
        }

        JAXBContext jc = JAXBContext.newInstance(Accounts.class);

        Unmarshaller u = jc.createUnmarshaller();
        Accounts wrapper = (Accounts) u.unmarshal(new StreamSource(is));
        return wrapper.getAccounts();
    }
}
