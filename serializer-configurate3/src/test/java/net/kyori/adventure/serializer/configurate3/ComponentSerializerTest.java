/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2020 KyoriPowered
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package net.kyori.adventure.serializer.configurate3;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.BlockNBTComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.EntityNBTComponent;
import net.kyori.adventure.text.KeybindComponent;
import net.kyori.adventure.text.ScoreComponent;
import net.kyori.adventure.text.SelectorComponent;
import net.kyori.adventure.text.StorageNBTComponent;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import ninja.leaping.configurate.ConfigurationNode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ComponentSerializerTest implements ConfigurateTestBase {

  @Test
  void testTextComponent() {
    final ConfigurationNode serialized = node(n -> {
      n.getNode(ComponentTypeSerializer.TEXT).setValue("Hello world");
    });
    assertEquals(serialized, serialize(TextComponent.of("Hello world")));
    assertEquals(TextComponent.of("Hello world"), deserialize(serialized));
  }

  @Test
  void testTranslatableComponent() {
    final ConfigurationNode serialized = node(n -> {
      n.getNode(ComponentTypeSerializer.TRANSLATE).setValue("translation.string");
      n.getNode(ComponentTypeSerializer.TRANSLATE_WITH).act(w -> {
        w.appendListNode().getNode(ComponentTypeSerializer.TEXT).setValue("test1");
        w.appendListNode().getNode(ComponentTypeSerializer.TEXT).setValue("test2");
      });
    });
    final Component component = TranslatableComponent.of("translation.string", TextComponent.of("test1"), TextComponent.of("test2"));

    assertEquals(serialized, serialize(component));
    assertEquals(component, deserialize(serialized));
  }

  @Test
  void testScoreComponent() {
    final ConfigurationNode serialized = node(n -> {
      n.getNode(ComponentTypeSerializer.SCORE).act(s -> {
        s.getNode(ComponentTypeSerializer.SCORE_NAME).setValue("Holder");
        s.getNode(ComponentTypeSerializer.SCORE_OBJECTIVE).setValue("some.objective");
        s.getNode(ComponentTypeSerializer.SCORE_VALUE).setValue("Override");
      });
    });
    final Component component = ScoreComponent.of("Holder", "some.objective", "Override");
    assertEquals(serialized, serialize(component));
    assertEquals(component, deserialize(serialized));
  }

  @Test
  void testScoreComponentNoValue() {
    final ConfigurationNode serialized = node(n -> {
      n.getNode(ComponentTypeSerializer.SCORE).act(s -> {
        s.getNode(ComponentTypeSerializer.SCORE_NAME).setValue("Holder");
        s.getNode(ComponentTypeSerializer.SCORE_OBJECTIVE).setValue("some.objective");
      });
    });
    final Component component = ScoreComponent.of("Holder", "some.objective");
    assertEquals(serialized, serialize(component));
    assertEquals(component, deserialize(serialized));
  }

  @Test
  void testKeybindComponent() {
    final ConfigurationNode serialized = node(n -> {
      n.getNode(ComponentTypeSerializer.KEYBIND).setValue("key.worldeditcui.toggle");
    });
    final Component component = KeybindComponent.of("key.worldeditcui.toggle");

    assertEquals(serialized, serialize(component));
    assertEquals(component, deserialize(serialized));
  }

  @Test
  void testSelectorComponent() {
    final ConfigurationNode serialized = node(n -> {
      n.getNode(ComponentTypeSerializer.SELECTOR).setValue("@e[limit=1]");
    });

    final Component component = SelectorComponent.of("@e[limit=1]");

    assertEquals(serialized, serialize(component));
    assertEquals(component, deserialize(serialized));
  }

  @Test
  void testBlockNBTComponent() {
    final ConfigurationNode serialized = node(n -> {
      n.getNode(ComponentTypeSerializer.NBT).setValue("Something[1].CustomName");
      n.getNode(ComponentTypeSerializer.NBT_INTERPRET).setValue(true);
      n.getNode(ComponentTypeSerializer.NBT_BLOCK).setValue("^0.0 ^0.0 ^0.0");
    });
    final Component component = BlockNBTComponent.builder()
      .nbtPath("Something[1].CustomName")
      .interpret(true)
      .localPos(0, 0, 0)
      .build();
    assertEquals(component, deserialize(serialized));
    assertEquals(serialized, serialize(component));
  }

  @Test
  void testEntityNBTComponent() {
    final ConfigurationNode serialized = node(n -> {
      n.getNode(ComponentTypeSerializer.NBT).setValue("Something[1].CustomName");
      n.getNode(ComponentTypeSerializer.NBT_INTERPRET).setValue(false);
      n.getNode(ComponentTypeSerializer.NBT_ENTITY).setValue("@e[limit=1]");
    });
    final Component component = EntityNBTComponent.builder()
      .nbtPath("Something[1].CustomName")
      .interpret(false)
      .selector("@e[limit=1]")
      .build();
    assertEquals(component, deserialize(serialized));
    assertEquals(serialized, serialize(component));
  }

  @Test
  void testStorageNBTComponent() {
    final ConfigurationNode serialized = node(n -> {
      n.getNode(ComponentTypeSerializer.NBT).setValue("Kittens.Toes[0]");
      n.getNode(ComponentTypeSerializer.NBT_INTERPRET).setValue(false);
      n.getNode(ComponentTypeSerializer.NBT_STORAGE).setValue("adventure:purr");
    });
    final Component component = StorageNBTComponent.builder()
      .nbtPath("Kittens.Toes[0]")
      .interpret(false)
      .storage(Key.of("adventure", "purr"))
      .build();
    assertEquals(component, deserialize(serialized));
    assertEquals(serialized, serialize(component));
  }

  @Test
  void testComponentWithChildren() {

  }

  @Test
  void testArrayChildren() {

  }
}
