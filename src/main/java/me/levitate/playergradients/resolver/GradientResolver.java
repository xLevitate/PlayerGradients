package me.levitate.playergradients.resolver;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import me.levitate.playergradients.gradients.Gradient;
import me.levitate.playergradients.gradients.GradientManager;
import org.bukkit.command.CommandSender;

public class GradientResolver extends ArgumentResolver<CommandSender, Gradient> {
    private final GradientManager gradientManager;

    public GradientResolver(GradientManager gradientManager) {
        this.gradientManager = gradientManager;
    }

    @Override
    protected ParseResult<Gradient> parse(Invocation<CommandSender> invocation, Argument<Gradient> context, String argument) {
        Gradient gradient = this.gradientManager.getGradientByName(argument);

        if (gradient == null) {
            return ParseResult.failure("Gradient not found");
        }

        return ParseResult.success(gradient);
    }

    @Override
    public SuggestionResult suggest(Invocation<CommandSender> invocation, Argument<Gradient> argument, SuggestionContext context) {
        return this.gradientManager.getGradientSet().stream()
                .map(Gradient::getName)
                .collect(SuggestionResult.collector());
    }

}
