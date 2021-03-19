package hints;

import org.springframework.nativex.hint.ProxyHint;
import org.springframework.nativex.hint.TypeHint;
import org.springframework.nativex.type.HintDeclaration;
import org.springframework.nativex.type.NativeConfiguration;
import org.springframework.nativex.type.TypeSystem;

import java.util.Collections;
import java.util.List;

@TypeHint(typeNames = {"com.example.demo.Foo"})
@ProxyHint(typeNames = {
	"com.example.demo.Animal",
	"org.springframework.aop.SpringProxy",
	"org.springframework.aop.framework.Advised",
	"org.springframework.core.DecoratingProxy"
})
public class MyHints implements NativeConfiguration {


	@Override
	public List<HintDeclaration> computeHints(TypeSystem typeSystem) {
		// look ma! dynamic hints!
		return Collections.emptyList();
	}
}
