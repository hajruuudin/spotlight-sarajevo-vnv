ALTER TABLE ss_media_store
    DROP CONSTRAINT ss_media_store_item_category;

ALTER TABLE ss_media_store
    ADD CONSTRAINT ss_media_store_item_category
        CHECK (((item_category)::text = ANY ((ARRAY['SPOT'::character varying, 'EVENT'::character varying, 'SECTION_GUIDE'::character varying, 'TRANSPORT'::character varying, 'ORGANISER'::character varying])::text[])));