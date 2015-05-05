/*
 * Copyright 2007-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fluxchess.jcpi.options;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class OptionsTest {

  @Test
  public void test() {
    AbstractOption option = Options.newHashOption(16, 4, 64);
    assertEquals("option name Hash type spin default 16 min 4 max 64", option.toString());

    option = Options.newNalimovPathOption("C:\\");
    assertEquals("option name NalimovPath type string default C:\\", option.toString());

    option = Options.newNalimovCacheOption(16, 4, 64);
    assertEquals("option name NalimovCache type spin default 16 min 4 max 64", option.toString());

    option = Options.newPonderOption(true);
    assertEquals("option name Ponder type check default true", option.toString());

    option = Options.newOwnBookOption(true);
    assertEquals("option name OwnBook type check default true", option.toString());

    option = Options.newMultiPVOption(1, 1, 8);
    assertEquals("option name MultiPV type spin default 1 min 1 max 8", option.toString());

    option = Options.newUciShowCurrLineOption(true);
    assertEquals("option name UCI_ShowCurrLine type check default true", option.toString());

    option = Options.newUciShowRefutationsOption(true);
    assertEquals("option name UCI_ShowRefutations type check default true", option.toString());

    option = Options.newUciLimitStrengthOption(true);
    assertEquals("option name UCI_LimitStrength type check default true", option.toString());

    option = Options.newUciEloOption(1500, 1200, 2500);
    assertEquals("option name UCI_Elo type spin default 1500 min 1200 max 2500", option.toString());

    option = Options.newUciAnalyseModeOption(true);
    assertEquals("option name UCI_AnalyseMode type check default true", option.toString());

    option = Options.newUciOpponentOption("none");
    assertEquals("option name UCI_Opponent type string default none", option.toString());

    option = Options.newUciEngineAboutOption("some text");
    assertEquals("option name UCI_EngineAbout type string default some text", option.toString());

    option = Options.newUciShredderbasesPathOption("C:\\");
    assertEquals("option name UCI_ShredderbasesPath type string default C:\\", option.toString());

    option = Options.newUciSetPositionValueOption("clearall");
    assertEquals("option name UCI_SetPositionValue type string default clearall", option.toString());
  }

}
