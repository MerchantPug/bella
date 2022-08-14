package com.github.merchantpug.bella.component;

import dev.onyxstudios.cca.api.v3.component.Component;

public interface IBellComponent extends Component {
	boolean hasBell();
	void setBell(boolean value);
}
