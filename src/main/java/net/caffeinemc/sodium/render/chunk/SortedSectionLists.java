package net.caffeinemc.sodium.render.chunk;

import java.util.Iterator;
import net.caffeinemc.sodium.render.chunk.state.SectionRenderFlags;

public class SortedSectionLists {
    
    private final SectionTree sectionTree;
    
    public final int[] terrainSectionIdxs;
    public final int[] blockEntitySectionIdxs;
    public final int[] tickingTextureSectionIdxs;
    public final int[] importantUpdatableSectionIdxs;
    public final int[] secondaryUpdatableSectionIdxs;
    
    public int terrainSectionCount;
    public int blockEntitySectionCount;
    public int tickingTextureSectionCount;
    public int importantUpdateSectionCount;
    public int secondaryUpdateSectionCount;
    
    public SortedSectionLists(SectionTree sectionTree) {
        this.sectionTree = sectionTree;
        
        int maxSections = sectionTree.getSectionTableSize();
        this.terrainSectionIdxs = new int[maxSections];
        this.blockEntitySectionIdxs = new int[maxSections];
        this.tickingTextureSectionIdxs = new int[maxSections];
        this.importantUpdatableSectionIdxs = new int[maxSections];
        this.secondaryUpdatableSectionIdxs = new int[maxSections];
    }
    
    public void reset() {
        this.terrainSectionCount = 0;
        this.blockEntitySectionCount = 0;
        this.tickingTextureSectionCount = 0;
        this.importantUpdateSectionCount = 0;
        this.secondaryUpdateSectionCount = 0;
    }
    
    public void addSectionIdx(int sectionIdx, byte flags) {
        if (SectionRenderFlags.has(flags, SectionRenderFlags.HAS_TERRAIN_MODELS)) {
            this.terrainSectionIdxs[this.terrainSectionCount++] = sectionIdx;
        }
    
        if (SectionRenderFlags.has(flags, SectionRenderFlags.HAS_BLOCK_ENTITIES)) {
            this.blockEntitySectionIdxs[this.blockEntitySectionCount++] = sectionIdx;
        }
    
        if (SectionRenderFlags.has(flags, SectionRenderFlags.HAS_TICKING_TEXTURES)) {
            this.tickingTextureSectionIdxs[this.tickingTextureSectionCount++] = sectionIdx;
        }
    
        if (SectionRenderFlags.has(flags, SectionRenderFlags.NEEDS_UPDATE)) {
            if (SectionRenderFlags.has(flags, SectionRenderFlags.NEEDS_UPDATE_IMPORTANT)) {
                this.importantUpdatableSectionIdxs[this.importantUpdateSectionCount++] = sectionIdx;
            } else {
                this.secondaryUpdatableSectionIdxs[this.secondaryUpdateSectionCount++] = sectionIdx;
            }
        }
    }
    
    private Iterator<RenderSection> createIterator(int[] idxs, int count) {
        return new Iterator<>() {
            private int i;
            
            @Override
            public boolean hasNext() {
                return this.i < count;
            }
    
            @Override
            public RenderSection next() {
                return SortedSectionLists.this.sectionTree.getSection(idxs[this.i++]);
            }
        };
    }
    
    public Iterable<RenderSection> getTerrainSections() {
        return () -> this.createIterator(this.terrainSectionIdxs, this.terrainSectionCount);
    }
    
    public Iterable<RenderSection> getBlockEntitySections() {
        return () -> this.createIterator(this.blockEntitySectionIdxs, this.blockEntitySectionCount);
    }
    
    public Iterable<RenderSection> getTickingTextureSections() {
        return () -> this.createIterator(this.tickingTextureSectionIdxs, this.tickingTextureSectionCount);
    }
    
    public Iterable<RenderSection> getImportantUpdatableSections() {
        return () -> this.createIterator(this.importantUpdatableSectionIdxs, this.importantUpdateSectionCount);
    }
    
    public Iterable<RenderSection> getSecondaryUpdatableSections() {
        return () -> this.createIterator(this.secondaryUpdatableSectionIdxs, this.secondaryUpdateSectionCount);
    }
}