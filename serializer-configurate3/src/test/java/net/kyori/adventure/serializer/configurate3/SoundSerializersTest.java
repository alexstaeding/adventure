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
import net.kyori.adventure.sound.Sound;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SoundSerializersTest implements ConfigurateTestBase {

  @Test
  void testSound() throws ObjectMappingException {
    final ConfigurationNode sound = node(n -> {
      n.getNode(SoundSerializer.NAME).setValue("minecraft:music_disc.cat");
      n.getNode(SoundSerializer.SOURCE).setValue("ambient");
      n.getNode(SoundSerializer.VOLUME).setValue(0.8f);
      n.getNode(SoundSerializer.PITCH).setValue(2.0f);
    });
    final Sound deserialized = Sound.of(Key.of("music_disc.cat"), Sound.Source.AMBIENT, 0.8f, 2.0f);
    assertEquals(sound, node().setValue(SoundSerializer.TYPE, deserialized));
    assertEquals(deserialized, sound.getValue(SoundSerializer.TYPE));
  }

  @Test
  void testSoundNoVolume() throws ObjectMappingException {
    final ConfigurationNode sound = node(n -> {
      n.getNode(SoundSerializer.NAME).setValue("minecraft:music_disc.cat");
      n.getNode(SoundSerializer.SOURCE).setValue("ambient");
      n.getNode(SoundSerializer.PITCH).setValue(2.0f);
    });
    final Sound deserialized = Sound.of(Key.of("music_disc.cat"), Sound.Source.AMBIENT, 1.0f, 2.0f);
    assertEquals(deserialized, sound.getValue(SoundSerializer.TYPE));
  }

  @Test
  void testSoundNoPitch() throws ObjectMappingException {
    final ConfigurationNode sound = node(n -> {
      n.getNode(SoundSerializer.NAME).setValue("minecraft:music_disc.cat");
      n.getNode(SoundSerializer.SOURCE).setValue("ambient");
      n.getNode(SoundSerializer.VOLUME).setValue(0.8f);
    });
    final Sound deserialized = Sound.of(Key.of("music_disc.cat"), Sound.Source.AMBIENT, 0.8f, 1.0f);
    assertEquals(deserialized, sound.getValue(SoundSerializer.TYPE));
  }

  @Test
  void testNoNameThrows() {
    assertThrows(ObjectMappingException.class, () -> node(n -> n.getNode(SoundSerializer.SOURCE).setValue("music")).getValue(SoundSerializer.TYPE));
  }

  @Test
  void testNoSourceThrows() {
    assertThrows(ObjectMappingException.class, () -> node(n -> n.getNode(SoundSerializer.NAME).setValue("music_disc.13")).getValue(SoundSerializer.TYPE));
  }
}
