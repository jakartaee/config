package jakarta.config.tck;

import jakarta.config.Config;
import jakarta.config.ConfigMapping;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConfigMappingTest {
    @Test
    void primitives() {
        Config config = Config.bootstrap();
        Primitives primitives = config.load(Primitives.class);

        assertNotNull(primitives);
        assertTrue(primitives.z());
        assertEquals('c', primitives.c());
        assertEquals(Byte.MAX_VALUE, primitives.b());
        assertEquals(Short.MAX_VALUE, primitives.s());
        assertEquals(Integer.MAX_VALUE, primitives.i());
        assertEquals(Long.MAX_VALUE, primitives.l());
        assertEquals(Float.MAX_VALUE, primitives.f());
        assertEquals(Double.MAX_VALUE, primitives.d());
    }

    @ConfigMapping("primitives")
    interface Primitives {
        boolean z();

        char c();

        byte b();

        short s();

        int i();

        long l();

        float f();

        double d();
    }

    @Test
    void groups() {
        Config config = Config.bootstrap();
        Groups groups = config.load(Groups.class);

        assertNotNull(groups);
        assertNotNull(groups.group());
        assertEquals("value", groups.group().value());
    }

    @ConfigMapping("groups")
    interface Groups {
        Group group();

        interface Group {
            String value();
        }
    }

    @Test
    void optionals() {
        Config config = Config.bootstrap();
        Optionals optionals = config.load(Optionals.class);

        assertNotNull(optionals);

        assertNotNull(optionals.s());
        assertTrue(optionals.s().isPresent());
        assertEquals("s", optionals.s().get());
        assertNotNull(optionals.missing());
        assertTrue(optionals.missing().isEmpty());
        assertNotNull(optionals.i());
        assertTrue(optionals.i().isPresent());
        assertEquals(Integer.MAX_VALUE, optionals.i().getAsInt());
        assertNotNull(optionals.l());
        assertTrue(optionals.l().isPresent());
        assertEquals(Long.MAX_VALUE, optionals.l().getAsLong());
        assertNotNull(optionals.d());
        assertTrue(optionals.d().isPresent());
        assertEquals(Double.MAX_VALUE, optionals.d().getAsDouble());

        assertNotNull(optionals.group());
        assertTrue(optionals.group().isPresent());
        assertEquals("value", optionals.group().get().value());
        assertNotNull(optionals.groupMissing());
        assertTrue(optionals.groupMissing().isEmpty());

        assertNotNull(optionals.list());
        assertTrue(optionals.list().isPresent());
        assertEquals(2, optionals.list().get().size());
        assertEquals("foo", optionals.list().get().get(0));
        assertEquals("bar", optionals.list().get().get(1));
        assertNotNull(optionals.listMissing());
        assertTrue(optionals.listMissing().isEmpty());

        assertNotNull(optionals.listGroup());
        assertTrue(optionals.listGroup().isPresent());
        assertEquals(2, optionals.listGroup().get().size());
        assertEquals("foo", optionals.listGroup().get().get(0).value());
        assertEquals("bar", optionals.listGroup().get().get(1).value());
        assertNotNull(optionals.listGroupMissing());
        assertTrue(optionals.listGroupMissing().isEmpty());
    }

    @ConfigMapping("optionals")
    interface Optionals {
        Optional<String> s();

        Optional<String> missing();

        OptionalInt i();

        OptionalLong l();

        OptionalDouble d();

        Optional<Group> group();

        Optional<Group> groupMissing();

        Optional<List<String>> list();

        Optional<List<String>> listMissing();

        Optional<List<Group>> listGroup();

        Optional<List<Group>> listGroupMissing();

        interface Group {
            String value();
        }
    }

    @Test
    void lists() {
        Config config = Config.bootstrap();
        Lists lists = config.load(Lists.class);

        assertNotNull(lists);

        assertNotNull(lists.list());
        assertEquals(2, lists.list().size());
        assertEquals("foo", lists.list().get(0));
        assertEquals("bar", lists.list().get(1));

        assertNotNull(lists.listGroups());
        assertEquals(2, lists.listGroups().size());
        assertEquals("foo", lists.listGroups().get(0).value());
        assertEquals("bar", lists.listGroups().get(1).value());

        assertNotNull(lists.listMaps());
        assertEquals(2, lists.listMaps().size());
        assertEquals("foo", lists.listMaps().get(0).get("foo"));
        assertEquals("bar", lists.listMaps().get(0).get("bar"));
        assertEquals("foo", lists.listMaps().get(1).get("foo"));
        assertEquals("bar", lists.listMaps().get(1).get("bar"));

        assertNotNull(lists.listMapsGroups());
        assertEquals(2, lists.listMapsGroups().size());
        assertEquals("foo", lists.listMapsGroups().get(0).get("foo").value());
        assertEquals("bar", lists.listMapsGroups().get(0).get("bar").value());
        assertEquals("foo", lists.listMapsGroups().get(1).get("foo").value());
        assertEquals("bar", lists.listMapsGroups().get(1).get("bar").value());
    }

    @ConfigMapping("lists")
    interface Lists {
        List<String> list();

        List<Group> listGroups();

        List<Map<String, String>> listMaps();

        List<Map<String, Group>> listMapsGroups();

        interface Group {
            String value();
        }
    }

    @Test
    void sets() {
        Config config = Config.bootstrap();
        Sets sets = config.load(Sets.class);

        assertNotNull(sets);

        assertNotNull(sets.set());
        assertEquals(2, sets.set().size());
        assertTrue(sets.set().contains("foo"));
        assertTrue(sets.set().contains("bar"));

        assertNotNull(sets.setGroups());
        assertEquals(2, sets.setGroups().size());
        assertTrue(sets.setGroups().stream().anyMatch(group -> group.value().equals("foo")));
        assertTrue(sets.setGroups().stream().anyMatch(group -> group.value().equals("bar")));
    }

    @ConfigMapping("sets")
    interface Sets {
        Set<String> set();

        Set<Group> setGroups();

        interface Group {
            String value();
        }
    }

    @Test
    void maps() {
        Config config = Config.bootstrap();
        Maps maps = config.load(Maps.class);

        assertNotNull(maps);

        assertNotNull(maps.map());
        assertEquals(2, maps.map().size());
        assertEquals("foo", maps.map().get("foo"));
        assertEquals("bar", maps.map().get("bar"));

        assertNotNull(maps.mapGroups());
        assertEquals(2, maps.mapGroups().size());
        assertEquals("foo", maps.mapGroups().get("foo").value());
        assertEquals("bar", maps.mapGroups().get("bar").value());

        assertNotNull(maps.mapListsGroups());
        assertEquals(2, maps.mapListsGroups().size());
        assertEquals("foo", maps.mapListsGroups().get("foo").get(0).value());
        assertEquals("bar", maps.mapListsGroups().get("bar").get(0).value());
    }

    @ConfigMapping("maps")
    interface Maps {
        Map<String, String> map();

        Map<String, Group> mapGroups();

        Map<String, List<Group>> mapListsGroups();

        interface Group {
            String value();
        }
    }
}
