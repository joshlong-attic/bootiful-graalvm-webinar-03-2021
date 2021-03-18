package myhints;

import org.springframework.nativex.hint.ProxyHint;
import org.springframework.nativex.hint.TypeHint;
import org.springframework.nativex.type.HintDeclaration;
import org.springframework.nativex.type.NativeConfiguration;
import org.springframework.nativex.type.TypeSystem;

import java.util.Collections;
import java.util.List;

@ProxyHint(
	typeNames = {
		"com.example.take2.Animal", "org.springframework.aop.SpringProxy",
		"org.springframework.aop.framework.Advised", "org.springframework.core.DecoratingProxy"
	}
)
@TypeHint(typeNames = "com.example.take2.Foo")
// todo @ResourceHint (banner.jpg) !!

public class MyHints implements NativeConfiguration {

	@Override
	public List<HintDeclaration> computeHints(TypeSystem typeSystem) {
		return Collections.emptyList();
	}
}
