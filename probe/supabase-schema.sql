create table if not exists public.shining_glasses_designs (
  id uuid primary key default gen_random_uuid(),
  library_id text not null default 'default',
  name text not null,
  width integer not null check (width = 36),
  height integer not null check (height = 12),
  cells jsonb not null,
  saved_at timestamptz not null default now(),
  updated_at timestamptz not null default now(),
  unique (library_id, name)
);

create table if not exists public.shining_glasses_animations (
  id uuid primary key default gen_random_uuid(),
  library_id text not null default 'default',
  name text not null,
  many_mode_byte integer not null default 1 check (many_mode_byte >= 0 and many_mode_byte <= 255),
  frames jsonb not null,
  saved_at timestamptz not null default now(),
  updated_at timestamptz not null default now(),
  unique (library_id, name)
);

create or replace function public.set_updated_at()
returns trigger
language plpgsql
as $$
begin
  new.updated_at = now();
  return new;
end;
$$;

drop trigger if exists set_shining_glasses_designs_updated_at on public.shining_glasses_designs;

create trigger set_shining_glasses_designs_updated_at
before update on public.shining_glasses_designs
for each row
execute function public.set_updated_at();

drop trigger if exists set_shining_glasses_animations_updated_at on public.shining_glasses_animations;

create trigger set_shining_glasses_animations_updated_at
before update on public.shining_glasses_animations
for each row
execute function public.set_updated_at();
