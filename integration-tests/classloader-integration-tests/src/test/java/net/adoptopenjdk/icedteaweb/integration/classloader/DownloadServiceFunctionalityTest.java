package net.adoptopenjdk.icedteaweb.integration.classloader;

import net.sourceforge.jnlp.runtime.classloader2.Extension;
import net.sourceforge.jnlp.runtime.classloader2.JnlpApplicationClassLoader;
import net.sourceforge.jnlp.runtime.classloader2.Part;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.util.List;

import static net.adoptopenjdk.icedteaweb.integration.classloader.ClassloaderTestUtils.CLASS_A;
import static net.adoptopenjdk.icedteaweb.integration.classloader.ClassloaderTestUtils.createFor;

public class DownloadServiceFunctionalityTest {

    @Test
    public void testPartDownloaded() throws Exception {
        //given
        final DummyJarProvider jarProvider = new DummyJarProvider();
        final List<Part> parts = createFor("integration-app-2.jnlp").getParts();

        //when
        final JnlpApplicationClassLoader classLoader = new JnlpApplicationClassLoader(parts, jarProvider);

        //than
        Assertions.assertFalse(classLoader.isPartDownloaded("lazy-package"));
    }

    @Test
    public void testExtensionPartDownloaded() throws Exception {
        //given
        final DummyJarProvider jarProvider = new DummyJarProvider();
        final List<Part> parts = createFor("integration-app-19.jnlp").getParts();
        final JnlpApplicationClassLoader classLoader = new JnlpApplicationClassLoader(parts, jarProvider);

        //when
        classLoader.loadClass(CLASS_A);

        //than
        final URL extensionURL = DownloadServiceFunctionalityTest.class.getResource("integration-app-19-extension.jnlp");
        final Extension extension = new Extension(extensionURL, null);
        Assertions.assertTrue(classLoader.isPartDownloaded("lazy-package", extension));
    }

    @Test
    public void testPartDownloaded2() throws Exception {
        //given
        final DummyJarProvider jarProvider = new DummyJarProvider();
        final List<Part> parts = createFor("integration-app-2.jnlp").getParts();
        final JnlpApplicationClassLoader classLoader = new JnlpApplicationClassLoader(parts, jarProvider);


        //when
        classLoader.loadClass(CLASS_A);

        //than
        Assertions.assertTrue(classLoader.isPartDownloaded("lazy-package"));
    }

    @Test
    public void testDownloadPart() throws Exception {
        //given
        final DummyJarProvider jarProvider = new DummyJarProvider();
        final List<Part> parts = createFor("integration-app-2.jnlp").getParts();
        final JnlpApplicationClassLoader classLoader = new JnlpApplicationClassLoader(parts, jarProvider);

        //when
        classLoader.downloadPart("lazy-package");

        //than
        Assertions.assertTrue(classLoader.isPartDownloaded("lazy-package"));
    }

    @Test
    public void testEagerPart() throws Exception {
        //given
        final DummyJarProvider jarProvider = new DummyJarProvider();
        final List<Part> parts = createFor("integration-app-21.jnlp").getParts();

        //when
        final JnlpApplicationClassLoader classLoader = new JnlpApplicationClassLoader(parts, jarProvider);

        //than
        Assertions.assertTrue(classLoader.isPartDownloaded("eager-package"));
    }

    @Test
    public void testDownloadPartFromExtension() throws Exception {
        //given
        final DummyJarProvider jarProvider = new DummyJarProvider();
        final List<Part> parts = createFor("integration-app-19.jnlp").getParts();
        final JnlpApplicationClassLoader classLoader = new JnlpApplicationClassLoader(parts, jarProvider);
        final URL extensionURL = DownloadServiceFunctionalityTest.class.getResource("integration-app-19-extension.jnlp");
        final Extension extension = new Extension(extensionURL, null);

        //when
        classLoader.downloadPart("lazy-package", extension);

        //than
        Assertions.assertTrue(classLoader.isPartDownloaded("lazy-package", extension));
        Assertions.assertFalse(classLoader.isPartDownloaded("lazy-package"));
        Assertions.assertEquals(1, jarProvider.getDownloaded().size());
    }
}