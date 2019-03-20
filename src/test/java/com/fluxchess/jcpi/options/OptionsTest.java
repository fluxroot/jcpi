/*
 * Copyright 2007-2019 The Java Chess Protocol Interface Project Authors
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

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class OptionsTest {

	@Test
	public void test() {
		AbstractOption option = Options.newHashOption(16, 4, 64);
		assertThat(option.toString()).isEqualTo("option name Hash type spin default 16 min 4 max 64");

		option = Options.newNalimovPathOption("C:\\");
		assertThat(option.toString()).isEqualTo("option name NalimovPath type string default C:\\");

		option = Options.newNalimovCacheOption(16, 4, 64);
		assertThat(option.toString()).isEqualTo("option name NalimovCache type spin default 16 min 4 max 64");

		option = Options.newPonderOption(true);
		assertThat(option.toString()).isEqualTo("option name Ponder type check default true");

		option = Options.newOwnBookOption(true);
		assertThat(option.toString()).isEqualTo("option name OwnBook type check default true");

		option = Options.newMultiPVOption(1, 1, 8);
		assertThat(option.toString()).isEqualTo("option name MultiPV type spin default 1 min 1 max 8");

		option = Options.newUciShowCurrLineOption(true);
		assertThat(option.toString()).isEqualTo("option name UCI_ShowCurrLine type check default true");

		option = Options.newUciShowRefutationsOption(true);
		assertThat(option.toString()).isEqualTo("option name UCI_ShowRefutations type check default true");

		option = Options.newUciLimitStrengthOption(true);
		assertThat(option.toString()).isEqualTo("option name UCI_LimitStrength type check default true");

		option = Options.newUciEloOption(1500, 1200, 2500);
		assertThat(option.toString()).isEqualTo("option name UCI_Elo type spin default 1500 min 1200 max 2500");

		option = Options.newUciAnalyseModeOption(true);
		assertThat(option.toString()).isEqualTo("option name UCI_AnalyseMode type check default true");

		option = Options.newUciOpponentOption("none");
		assertThat(option.toString()).isEqualTo("option name UCI_Opponent type string default none");

		option = Options.newUciEngineAboutOption("some text");
		assertThat(option.toString()).isEqualTo("option name UCI_EngineAbout type string default some text");

		option = Options.newUciShredderbasesPathOption("C:\\");
		assertThat(option.toString()).isEqualTo("option name UCI_ShredderbasesPath type string default C:\\");

		option = Options.newUciSetPositionValueOption("clearall");
		assertThat(option.toString()).isEqualTo("option name UCI_SetPositionValue type string default clearall");
	}

}
