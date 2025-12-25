LDLib / AE2 Chunk Meshing Crash Patch
What this patch does

Simple Explanation:
Stops the game from crashing with the following error with ae2+embeddium/ldlib: 

Description: Encountered exception while building chunk meshes

Compatibility (must be running these versions or recompile is needed):

appliedenergistics2-forge-15.4.10.jar

ldlib-forge-1.20.1-1.0.40.b.jar

Description: Encountered exception while building chunk meshes

This mod applies a targeted client-side mixin to Applied Energistics 2’s
CableBusBlock.getAppearance method to prevent intermittent client crashes during chunk rendering when using multi-threaded renderers such as Embeddium/Sodium.

Specifically, the patch:

Intercepts AE2’s call to ModelDataManager.getAt(BlockPos)

Guards the call against ConcurrentModificationException

Returns ModelData.EMPTY as a safe fallback when a concurrent modification is detected

Logs a one-time warning when the fix is first triggered, making it easy to verify that the patch is active without spamming logs

This prevents the game from crashing to desktop while chunk meshes are being built on worker threads.

Why this is needed

Modern rendering pipelines (Embeddium/Sodium) build chunk meshes on background threads.
AE2’s CableBusBlock.getAppearance accesses ModelDataManager, which is not thread-safe and internally iterates over a mutable HashMap.

Under heavy load or prolonged play sessions, this leads to:

java.util.ConcurrentModificationException
    at ModelDataManager.refreshAt
    at ModelDataManager.getAt
    at CableBusBlock.getAppearance


Because this exception occurs on a render worker thread, it crashes the entire client.

Design goals

Minimal & non-invasive: Does not modify AE2 logic or data structures

Fail-safe rendering: Prefers visual degradation over client crashes

AE2-specific: Targets only CableBusBlock.getAppearance

Client-only: No server impact, no gameplay changes

What this patch does not do

It does not make ModelDataManager thread-safe

It does not change AE2 behavior on the main thread

It does not fix the underlying concurrency issue inside Forge’s ModelDataManager

This is a defensive compatibility patch intended to keep the client stable until an upstream fix is available.

When you’ll see it working

When triggered, the log will contain a one-time message like:

AE2AsyncModelFix ACTIVE: CableBusBlock.getAppearance on thread 'Chunk Render Task Executor #X'

If a concurrent modification is detected, the crash is prevented and rendering continues safely.

