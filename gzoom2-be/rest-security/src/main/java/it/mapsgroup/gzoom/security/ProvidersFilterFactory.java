package it.mapsgroup.gzoom.security;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.security.authentication.AuthenticationProvider;

import java.util.List;

import static java.util.Collections.*;
import static java.util.stream.Collectors.*;

/**
 * Filters a list of providers and saves only those that are enabled.
 *
 * @author Fabio G. Strozzi
 */
public class ProvidersFilterFactory implements FactoryBean<List<AuthenticationProvider>> {
    private final List<AuthenticationProvider> providers;

    public ProvidersFilterFactory(List<AuthenticationProvider> all) {
        providers = unmodifiableList(filter(all));
    }

    private List<AuthenticationProvider> filter(List<AuthenticationProvider> all) {
        return all.stream()
                .filter(ProvidersFilterFactory::isEnabled)
                .collect(toList());
    }

    private static boolean isEnabled(AuthenticationProvider ap) {
        return !(ap instanceof PluggableAuthenticationProvider) || ((PluggableAuthenticationProvider) ap).isEnabled();
    }

    @Override
    public List<AuthenticationProvider> getObject() throws Exception {
        return providers;
    }

    @Override
    public Class<?> getObjectType() {
        return List.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
